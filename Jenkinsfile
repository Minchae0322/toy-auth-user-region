pipeline {
    agent any
    environment {
        BACKUP_DIR = "/backup/jenkins_builds/${JOB_NAME}" // 백업 기본 디렉토리
    }
    stages {
        stage('Prepare Version') {
            steps {
                script {
                      // 날짜 및 시간 형식 지정 (예: 20250114-153000)
                    def timestamp = new Date().format("yyyyMMdd-HHmmss", TimeZone.getTimeZone('UTC'))
                    // 빌드 버전 정보 생성
                    def commitHash = env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : 'manual'
                    env.BUILD_VERSION = "${BUILD_NUMBER}-${commitHash}"
                    echo "Current Build Version: ${env.BUILD_VERSION}-${timestamp}"
                }
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
            }
        }
    }
    post {
        failure {
            script {
                // 빌드 실패 시 복구
                sh '''
                if [ -d "${BACKUP_DIR}/backup-${BUILD_VERSION}" ]; then
                    cp -r ${BACKUP_DIR}/backup-${BUILD_VERSION}/* build/libs/
                    echo "Build failed. Restored artifacts from backup-${BUILD_VERSION}"
                else
                    echo "No backup available to restore."
                fi
                '''
            }
        }
        success {
            script {
                echo "Build succeeded with version: ${env.BUILD_VERSION}"
                // 빌드 결과물 저장
                sh '''
                mkdir -p ${BACKUP_DIR}/${BUILD_VERSION}
                cp build/libs/*.jar ${BACKUP_DIR}/${BUILD_VERSION}/
                echo "Current build artifacts saved with version: ${BUILD_VERSION}"
                '''
            }
        }
    }
}
