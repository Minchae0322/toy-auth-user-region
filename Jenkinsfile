pipeline {
    agent any
    environment {
        BACKUP_DIR = "/backup/jenkins_builds/${JOB_NAME}" // 백업 기본 디렉토리
        PREVIOUS_VERSION_FILE = "/backup/jenkins_builds/${JOB_NAME}/last_build_version.txt" // 이전 버전 기록 파일
    }
    stages {
        stage('Prepare Version') {
            steps {
                script {
                    // 날짜 및 시간 형식 지정 (예: 20250114-153000)
                    def timestamp = new Date().format("yyyyMMdd-HHmmss", TimeZone.getTimeZone('UTC'))
                    
                    // 빌드 버전 정보 생성
                    def commitHash = env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : 'manual'
                    env.BUILD_VERSION = "${BUILD_NUMBER}-${commitHash}-${timestamp}"
                    echo "Current Build Version: ${env.BUILD_VERSION}"
                    
                    // 이전 빌드 버전 저장
                    if (fileExists(env.PREVIOUS_VERSION_FILE)) {
                        env.PREVIOUS_BUILD_VERSION = readFile(env.PREVIOUS_VERSION_FILE).trim()
                        echo "Previous Build Version: ${env.PREVIOUS_BUILD_VERSION}"
                    } else {
                        echo "No previous build version found."
                    }
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
                // 빌드 실패 시 이전 버전으로 복구
                if (env.PREVIOUS_BUILD_VERSION) {
                    echo "Restoring previous build version: ${env.PREVIOUS_BUILD_VERSION}"
                    sh '''
                    if [ -d "${BACKUP_DIR}/${PREVIOUS_BUILD_VERSION}" ]; then
                        cp -r ${BACKUP_DIR}/${PREVIOUS_BUILD_VERSION}/* build/libs/
                        echo "Restored previous build artifacts from ${PREVIOUS_BUILD_VERSION}"
                    else
                        echo "No backup available for previous build version: ${PREVIOUS_BUILD_VERSION}"
                    fi
                    '''
                } else {
                    echo "No previous build version available to restore."
                }
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
                
                // 현재 빌드 버전을 기록
                echo "${BUILD_VERSION}" > ${PREVIOUS_VERSION_FILE}
                '''
            }
        }
    }
}
