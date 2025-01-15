pipeline {
    agent any
    environment {
        BACKUP_DIR = "/backup/jenkins_builds/${JOB_NAME}" // 백업 기본 디렉토리
        BUILD_VERSION = "${BUILD_NUMBER}-${env.GIT_COMMIT?.take(7) ?: 'manual'}" // 버전 정보
    stages {
        stage('Prepare Version') {
            steps {
                script {
                    // 버전 정보를 출력
                    echo "Current Build Version: ${BUILD_VERSION}"
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
            echo "Build succeeded with version: ${BUILD_VERSION}"
            script {
                    sh '''
                    # 빌드 결과물 저장
                    mkdir -p ${BACKUP_DIR}/${BUILD_VERSION}
                    cp build/libs/*.jar ${BACKUP_DIR}/${BUILD_VERSION}/
                    echo "Current build artifacts saved with version: ${BUILD_VERSION}"
                    '''
                }
        }
    }
}
