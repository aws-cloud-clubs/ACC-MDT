
## 2024 ACC 해커톤 마돈탕팀 : 파일공유 시스템

### 🧑‍🤝‍🧑팀원
| [김효준(팀장)](https://github.com/Khyojun)|[박채연](https://github.com/Yeon-chae)|[김수진](https://github.com/cowboysj)|[부준혁](https://github.com/)|[정지환](https://github.com/)|[조정원](https://github.com/)|
| :-------:| :-------:| :-------:| :-------:| :-------:| :-------:|
|백엔드|백엔드|백엔드|인프라|인프라|인프라|

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

## 📂패키지 구조 
