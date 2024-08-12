
## 2024 ACC 해커톤 마돈탕팀 : 파일공유 시스템

### 🧑‍🤝‍🧑 팀원
| [김효준(팀장)](https://github.com/Khyojun)|[박채연](https://github.com/Yeon-chae)|[김수진](https://github.com/cowboysj)|[부준혁](https://github.com/)|[정지환](https://github.com/)|[조정원](https://github.com/)|
| :-------:| :-------:| :-------:| :-------:| :-------:| :-------:|
|백엔드|백엔드|백엔드|인프라|인프라|인프라|

### 📜 요구사항 
1. 서비스 유저 수는 약 1000명이다.
2. 매일 약 100~200개의 새로운 파일이 업로드된다.
3. 파일 당 평균 크기는 약 50MB다.
4. 사용자는 파일을 업로드, 조회, 다운로드, 검색할 수 있다.
5. 사용자는 파일을 공유할 수 있는 링크를 생성할 수 있다.
   
## 🏗️ 아키텍처 설계

### 📄 아키텍처 요구사항 
1. 고가용성을 위해 Availability Zone을 이중화해야 한다.
2. 서비스 유저 수인 1000명이 한 번에 몰릴 경우를 대비해 서버의 부하를 분산해야 한다.
3. 개발자가 private EC2에 접근할 수 있어야 한다.
4. 파일 시스템 특성 상, 사용자가 오랫동안 접근하지 않는 파일이 있기 때문에 스토리지를 효율적으로 사용해야 한다.

<img width="938" alt="image" src="https://github.com/user-attachments/assets/00af6c6c-5ac5-4aeb-ad81-7a3c5fa7fd6b">

1. 사용자가 요청을 보내면 ALB가 각 가용영역에 있는 Private Subnet의  EC2 인스턴스로 분산시킨다.
2. 개발자는 Session Manager를 통해 EC2 인스턴스에 접근해 작업을 수행할 수 있다.

> **파일 업로드**
1. 사용자가 파일 업로드 요청을 보내면, EC2 인스턴스를 통해 S3 버킷에 저장된다.
2. S3에 파일이 저장되면, S3 객체 생성 이벤트가 Lambda 함수를 트리거한다.
3. Lambda 함수는 업로드된 파일에 대한 메타데이터(파일 이름, 사용자, 확장자, 생성날짜,파일 사이즈)를 추출해 DynamoDB에 저장한다.

> **파일 조회, 다운로드**
1. 사용자가 파일 조회, 다운로드 요청을 보내면 EC2 인스턴스(Springboot 서버)가 S3의 presignedUrl을 제공한다.
2. 사용자는 해당 링크를 통해 직접 파일을 조회(미리보기), 다운로드할 수 있다.

> **파일 검색**
1. 사용자가 자신이 올린 파일들에 대해 검색 요청을 보낸다.
2. EC2 인스턴스에서 해당 조건에 맞는 파일을 DynamoDB에서 찾아 반환한다.


#### VPC, Subnet, ALB
<img width="829" alt="image" src="https://github.com/user-attachments/assets/ff9caeba-a4ae-449b-989a-62065a33fe0e">

- VPC(10.4.0.0/16)를 생성했습니다.
- 고가용성을 위해 AZ를 ap-northeast-2a, ap-northeast-2c로 구성했습니다.
- 각 AZ에 Public Subnet과 Private Subnet을 구성 후 Private Subnet에 EC2를 생성했습니다.
- ALB를 구성하기 위해선 각 가용영역에 Public Subnet이 필요하기 때문에 가용영역 a에 Public Subnet01, 가용영역 c에 Public Subnet02를 구성하고 ALB를 사용해서 분산했습니다.


#### Route53
<img width="830" alt="image" src="https://github.com/user-attachments/assets/eea92d3f-8eae-42c7-9daa-e43fe57e5416">
 
Route53을 이용해 도메인을 연결했습니다.

#### VPC Gateway Endpoint
<img width="821" alt="image" src="https://github.com/user-attachments/assets/ac06180f-d389-4b27-b841-0beb21604e3b">

VPC Gateway Endpoint를 사용해 Session Manager, DynamoDB, S3를 연결했습니다.

#### Session Manager
- 개발자가 Private EC2에 안전하게 접속하기 위해 Session Manager를 사용했습니다.
- 처음에는 Public Subnet에 위치한 EC2 인스턴스인 Bastion Host를 사용했지만 인스턴스 비용이 나오고 private EC2 설정이 어렵다는 단점을 느껴, Session Manager를 사용하는 것으로 변경했습니다.

#### S3
- S3를 통해 파일을 관리했습니다.
- 자주 액세스되지 않는 데이터를 자동으로 더 싼 스토리지 계층으로 이동시키기 위해 Intelligent-Tiering을 사용했습니다.
- <img width="817" alt="image" src="https://github.com/user-attachments/assets/26bc0975-b472-478b-b7ac-78afccd5d69b">

#### DynamoDB
파일의 메타데이터만을 저장하면 되기 때문에, RDS 대신 DynamoDB를 사용했습니다.

## 💾 백엔드 고려사항

📁 **업로드 시** **`S3 PresignedUrl 사용해 업로드` vs `서버에서 직접 S3에 파일 업로드`**

1. spring boot에서 직접 S3에 파일 업로드
    - 방식 1 : Stream 업로드
        - 장점 : 힙 메모리나 디스크를 거의 사용하지 않는다. 
        - 단점 : 업로드 속도가 굉장히 느리고, api 요청 한개 당 한 개의 파일만 전송이 가능하다.
    - 방식 2 : multipart 업로드
        - 장점: Stream 업로드에 비해 높은 추상화, 단일 api 요청에 여러 개의 파일 전송이 가능하다.
        - 단점: 클라이언트가 전송한 파일을 들고 있는 과정에서 서버의 디스크를 사용한다.
2. S3 presignedUrl을 사용해 업로드
    - 서버에서 직접 파일을 S3에 업로드하는 방식과 달리, presignedUrl을 사용하면 AWS 가 multipart 방식을 구현해준다.

**⇒ 따라서 presignedUrl을 사용해 업로드하는 방식 선택**

 📁 **PresignedUrl로 S3에 파일을 업로드하는 주체 : `클라이언트` vs `서버`**

클라이언트가 파일 업로드 로직을 담당한다면, 요청이 많이 들어올 때 서버가 더 많은 요청을 처리할 수 있다.

서버는 클라이언트가 전송한 파일을 넘겨받는 로직을 담당할 필요가 없다.

> **고려사항 : 메타데이터 전송 방법**
> 
- 클라이언트에서 S3에 파일을 업로드하는 로직을 선택한다면 서버에서는 파일의 메타데이터를 알 수 없기 때문에 Lambda Trigger로 S3에 업로드가 감지되면 DynamoDB에 전송하는 방식을 사용해야 함
- 서버에서 S3에 파일을 업로드하는 로직을 선택한다면 서버에서 메타데이터를 DynamoDB에 전송하는 방식을 사용해야 함

**⇒ 요청이 많이 들어온다면, 클라이언트가 직접 파일을 업로드하고, Lambda를 사용해 서버리스로 DynamoDB에 메타데이터를 삽입하는 방식이 더 적절하다고 판단해 해당 방식 선택**

### API
> 파일 업로드
- 사용자가 파일 업로드 요청을 보내면, S3 presignedURL을 반환한다.
- 반환된 presignedURL로 S3로 직접 파일을 업로드할 수 있다.
  
| Method | Endpoint | body | response |
| --- | --- | --- | --- |
| POST | /api/v1/file/upload | userId, fileName | 업로드할 s3 presigned URL |

> 파일 조회
- 사용자가 파일 조회 요청을 보내면, S3 presignedURL을 반환한다.
- 반환된 presignedURL로 파일을 브라우저 내에서 조회할 수 있다.
  
| Method | Endpoint | body | response |
| --- | --- | --- | --- |
| POST | /api/v1/file | filePath | 조회할 s3 presigned URL |

> 파일 다운로드
- 사용자가 파일 다운로드 요청을 보내면, 다운로드용 `Content-Disposition: attachment` S3 presignedURL을 반환한다.
- 반환된 presignedURL로 파일을 다운받을 수 있다.
  
| Method | Endpoint | body | response |
| --- | --- | --- | --- |
| POST | /api/v1/download | filePath | 다운로드할 s3 presigned URL |



> 파일 검색
- 사용자가 파일 검색 요청을 보내면 서버가 DynamoDB에 저장된 데이터를 기반으로 파일을 찾아 반환한다.
  
| Method | Endpoint | RequestParam | response |
| --- | --- | --- | --- |
| GET | /api/v1/search | userId, fileNam | 파일 경로  |



## 부하 테스트 
- k6를 이용해 진행하였고, 서버 로그 상으로도 전부 성공하는 걸 확인했습니다.


|검색|조회|
| :-------| :-------|
|1000명이 1초 동안 요청|1000명이 10초 동안 요청|
|<img width="829" alt="image" src="https://github.com/user-attachments/assets/14b0958e-093b-414b-bc0c-ac224477aaaa">|<img width="742" alt="image" src="https://github.com/user-attachments/assets/835d2f7f-109d-4d1c-9ac4-9c9c07eba8b4">|
|- 1초 만에 1000명의 사용자의 1000개의 요청이 다 처리됨 <br> - 모든 요청이 1초 이내에 다 처리됨 <br> - 평균 요청 처리 시간 : 136.24ms|- 10초 동안 1000명의 사용자가 요청을 보냄. 총 7935개의 요청 다 처리됨 <br> - 7935개의 요청 중 6개의 요청만 1초가 넘음  <br> - 평균 요청 처리 시간 : 156.85ms|


|다운로드|업로드|
| :-------| :-------|
|1000명이 10초 동안 요청|1000명이 10초 동안 요청|
|<img width="751" alt="image" src="https://github.com/user-attachments/assets/3f40e708-bdc6-4a89-815e-eb64f2b67fd1">|<img width="745" alt="image" src="https://github.com/user-attachments/assets/20a77ce6-3743-48de-bc4a-caa18494ec72">|
|- 10초 동안 1000명의 사용자가 요청을 보냄. 총 8685개의 요청이 다 처리됨 <br> - 8685개의 요청 중 16개의 요청만 1초가 넘음 <br> - 평균 요청 처리 시간 : 153.8ms|- 10초 동안 1000명의 사용자가 요청을 보냄. 총 8631개의 요청이 다 처리됨 <br> - 8631개의 요청이 모두 1초 이내에 처리됨 <br> - 평균 요청 처리 시간 : 22.28ms|




## 백엔드 컨벤션
### **🗂️ Commit Convention**

- 형식: `[태그]: 커밋내용`

| 태그 이름 | 설명                                                          |
| :-------: | :------------------------------------------------------------ |
|   Feat    | 새로운 기능 구현                                              |
|    Fix    | 버그, 오류 해결                                               |
|   Docs    | README나 WIKI 등의 문서 수정                                  |
| Refactor  | 코드 리팩토링                                                 |
|   Test    | 테스트 코드, 리펙토링 테스트 코드 추가                        |
|   Chore   | 빌드 업무 수정, 패키지 매니저 수정, 간단한 코드 수정, 내부 파일 수정 |
|   Add     | 파일 추가 |
|   CI/CD    | CI/CD 구현 |

### **🐈‍⬛ Git Flow**

| 브랜치 명 | 설명 |
| :-------: | :--------------------------------------------------------------------------------- |
| main | 소프트웨어 제품 배포하는 용도로 쓰는 브랜치 |
| develop | 개발용 브랜치 |
| feature | 단위 기능 개발용 브랜치 |

<br>

 **작업 시작 시 선행되어야 할 작업**

1. issue를 생성
2. feature branch를 생성 (ex.feature/#11)
3. add → commit → push → pull request 
4. pull request를 develop branch로 merge 
6. 이전에 merge된 작업이 있을 경우 다른 branch에서 진행하던 작업에 merge된 작업을 pull하기
7. 종료된 issue와 pull request의 label을 관리
