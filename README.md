##jenkins webhook trigger : branch main 이 업데이트 될 때


### Ubuntu Jenkins 설치

`curl -fsSL https://pkg.jenkins.io/debian/jenkins.io-2023.key | sudo tee \
/usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
https://pkg.jenkins.io/debian binary/ | sudo tee \
/etc/apt/sources.list.d/jenkins.list > /dev/null`

`sudo apt update`

`sudo apt install jenkins`

초기 비밀번호 확인 : sudo cat /var/lib/jenkins/secrets/initialAdminPassword

포트열기 :

sudo ufw status

*# Status: inactive 라면*

sudo ufw allow 8080

sudo ufw allow OpenSSH

sudo ufw enable

sudo ufw status
