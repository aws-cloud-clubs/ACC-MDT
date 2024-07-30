import json
import boto3
from urllib.parse import unquote_plus
from datetime import datetime
from botocore.exceptions import ClientError

s3 = boto3.client('s3')
dynamodb = boto3.resource('dynamodb')
table_name = # DynamoDB  table name 입력하기
table = dynamodb.Table(table_name)

def lambda_handler(event, context):
    for record in event['Records']:
        bucket_name = record['s3']['bucket']['name']
        object_key = unquote_plus(record['s3']['object']['key'])
        
        user_id, file_name = object_key.split('/', 1)
        
        try:
            response = s3.head_object(Bucket=bucket_name, Key=object_key)
            metadata = response['Metadata']
            content_type = response['ContentType']
            content_length = response['ContentLength']
            last_modified = response['LastModified']
            created_at = last_modified.isoformat()

            try:
                table.update_item(
                    Key={
                        'user_id': user_id,
                        'file_name': file_name
                    },
                    UpdateExpression="set created_at = :created_at, file_content_length = :content_length, file_size = :file_size, file_type = :file_type",
                    ExpressionAttributeValues={
                        ':created_at': created_at,
                        ':content_length': content_length,
                        ':file_size': content_length,  
                        ':file_type': content_type
                    },
                    ConditionExpression="attribute_exists(user_id) AND attribute_exists(file_name)",
                    ReturnValues="ALL_NEW"
                )
                print(f"Metadata for {object_key} updated in DynamoDB table {table_name}")
            except ClientError as e:
                if e.response['Error']['Code'] == 'ConditionalCheckFailedException':
                    table.put_item(
                        Item={
                            'user_id': user_id,
                            'created_at': created_at,
                            'file_content_length': content_length,
                            'file_name': file_name,
                            'file_size': content_length, 
                            'file_type': content_type
                        }
                    )
                    print(f"Metadata for {object_key} inserted in DynamoDB table {table_name}")
                else:
                    raise

        except Exception as e:
            print(f"Error processing object {object_key} from bucket {bucket_name}. Error: {str(e)}")
            raise e

    return {
        'statusCode': 200,
        'body': json.dumps('Lambda function executed successfully!')
    }
