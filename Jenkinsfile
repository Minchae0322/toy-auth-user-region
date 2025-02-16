pipeline {
    agent any

    environment {
        BACKUP_DIR = "/backup/jenkins_builds/${JOB_NAME}" // 백업 기본 디렉토리
        PREVIOUS_VERSION_FILE = "/backup/jenkins_builds/${JOB_NAME}/last_build_version.txt" // 이전 빌드 기록 파일
        DOCKER_IMAGE = "auth-image:${BUILD_NUMBER}" // Docker 이미지 태그
        DOCKER_NETWORK = "app-network" // Docker 네트워크 이름
        ENV_FILE = "toy.env" // 환경 변수 파일명
    }

    stages {
        stage('Prepare Version') {
            steps {
                script {
                    def timestamp = new Date().format("yyyyMMdd-HHmmss", TimeZone.getTimeZone('UTC'))
                    def commitHash = env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : 'manual'
                    env.BUILD_VERSION = "${BUILD_NUMBER}-${commitHash}-${timestamp}"
                    echo "Current Build Version: ${env.BUILD_VERSION}"

                    if (fileExists(env.PREVIOUS_VERSION_FILE)) {
                        env.PREVIOUS_BUILD_VERSION = readFile(env.PREVIOUS_VERSION_FILE).trim()
                        echo "Previous Build Version: ${env.PREVIOUS_BUILD_VERSION}"
                    } else {
                        echo "No previous build version found."
                    }
                }
            }
        }

        stage('Load Environment Variables') {
            steps {
                script {
                    // toy.env 파일이 있는지 확인하고, 있으면 환경 변수로 로드
                    if (fileExists("toy.env")) {
                        sh 'export $(grep -v "^#" toy.env | xargs)'
                        echo "Loaded environment variables from toy.env file."
                    } else {
                        echo "No toy.env file found!"
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

        stage('Docker Build') {
            steps {
                script {
                    sh '''
                    docker build -t ${DOCKER_IMAGE} --build-arg JAR_FILE=build/libs/*.jar .
                    '''
                }
            }
        }

        stage('Docker Run') {
            steps {
                script {
                    // Docker 네트워크가 존재하는지 확인하고, 없으면 생성
                    sh '''
                    if ! docker network ls | grep -q ${DOCKER_NETWORK}; then
                        docker network create ${DOCKER_NETWORK}
                    fi
                    '''

                    // 기존 컨테이너 중지 및 삭제 후 새 컨테이너 실행
                    sh '''
                    docker stop auth-container || true
                    docker rm auth-container || true
                    docker run -d \
                        --name auth-container \
                        --network ${DOCKER_NETWORK} \
                        -p 8081:8081 \
                        --env-file toy.env \
                        ${DOCKER_IMAGE}
                    '''
                }
            }
        }
    }

    post {
        failure {
            script {
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
                sh '''
                mkdir -p ${BACKUP_DIR}/${BUILD_VERSION}
                cp build/libs/*.jar ${BACKUP_DIR}/${BUILD_VERSION}/
                echo "${BUILD_VERSION}" > ${PREVIOUS_VERSION_FILE}
                echo "Current build artifacts saved with version: ${BUILD_VERSION}"
                '''
            }
        }
    }
}
