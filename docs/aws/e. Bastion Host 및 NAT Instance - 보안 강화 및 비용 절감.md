# Bastion Host 및 NAT Instance - 보안 강화 및 비용 절감

## ⚡️ Bastion Host

서버의 보안 취약점을 보완하기 위해 Bastion Host 를 사용한다.

> 서버보다 앞에 위치해 외부의 직접적인 접근을 막아주거나 대신 받는 역할을 하는 인스턴스

- **서버를 `Private Subnet` 에 배치해서 외부의 직접적인 접근을 차단**
- ️`Bastion Host`를 `Public Subnet` 에 배치
- `️Bastion Host`를 통해 `Private Subnet` 의 서버로 접근

---

<img width="400" alt="스크린샷 2024-10-22 오전 1 46 23" src="https://github.com/user-attachments/assets/65e343ff-c8ff-430d-9dec-e52c009a9b08">

- 지금까지의 구조에서는 외부에서 바로 `Public Subnet` 의 서버로 접근이 가능했다.

<img width="400" alt="스크린샷 2024-10-22 오전 1 46 38" src="https://github.com/user-attachments/assets/9fa6753d-13b8-4e13-a69a-0b4d69c3f785">

- 누군가가 악의적으로 DDoS, 트래픽 변조 같은 공격을 시도할 수 있다. 서비스가 불가능해질 수 있다.

<img width="400" alt="스크린샷 2024-10-22 오전 1 46 50" src="https://github.com/user-attachments/assets/c12bcb0d-aa2b-4401-83b3-f9577a2b362d">

- 운영되는 서버는 `Private Subnet` 으로 이전시키고, `Public Subnet` 에 `Bastion Host` 를 배치한다.
- `Bastion Host` 로 `SSH` 접속을 하고, 다시 `Bastion Host` 를 통해 `Private Subnet` 의 서버로 `SSH 접속`을 하면 이전과 똑같이 서버에 접속할 수 있다.
- 여기서 중요한 부분은 `서버`와 `Bastion Host` 각각 `보안 그룹`을 가지고 있어야 한다.
- `Bastion Host` 는 `SSH Port` 만 열어놓고, `서버`는 `SSH Port` 를 `Bastion Host의 보안그룹`을 대상으로 열어둔다.
  - 가장 좋은건 `Bastion Host 보안 그룹`에 **SSH Port 접근을 허용할 IP** 만 열어주는게 좋다.

<br/>

<br/>

<br/>

## ⚡️ Bastion Host EC2 Instance 생성 및 Private Subnet 으로 서버 이전

기존의 ec2 인스턴스는 삭제해주고, 새로운 ec2 인스턴스를 생성한다.

<img width="1000" alt="스크린샷 2024-10-22 오후 10 32 51" src="https://github.com/user-attachments/assets/ed551500-c69c-4869-83da-484c4cf23c26">

- 이전에 EC2 인스턴스 생성과 똑같이 `Bastion Host` 를 생성한다.
  - Key Pair 는 Bastion Host 전용으로 새로 생성해줘도 되고, 편한 관리를 위해 기존의 것을 사용해도 된다. 
  - `Public IP` 를 `Enable`(활성화) 해준다.
  - `Network Settings` 에서 `ssh(port 22)` 접근을 허용하는 `Security Group(sg)` 을 생성해주는 것만 다르다.


<img width="1000" alt="스크린샷 2024-10-22 오후 10 57 31" src="https://github.com/user-attachments/assets/2df619b9-f774-498c-b96e-f57f0f0429a0">

진짜 서버를 배포할 Private EC2 Instance 를 생성한다.
- `Public IP` 를 `Disable`(비활성화) 해준다.
  - 이렇게 설정하면 외부에서 EC2 인스턴스로 접근이 불가능하게 된다.
- SSH 에 대한 접근을 Bastion Host 보안그룹(sg) 에만 허용해준다.
  - 보안그룹의 허용 대사은 IP 주소나 CIDR 외에도 다른 보안그룹 자체를 지정해줄 수 있다.
  - 소스타입을 Custom 으로 변경하면 다른 보안그룹을 지정할 수 있다.


<img width="1000" alt="스크린샷 2024-10-22 오후 11 06 44" src="https://github.com/user-attachments/assets/71f25063-dfe7-4b97-8098-cda882db0ce0">

`Advanced Settings` 에 `User Data` 부분이 있다.
- `User Data` 는 EC2 인스턴스가 시작될 때 실행되는 `스크립트`를 입력할 수 있다.
- 이전에 terminal 에 직접 입력했던, **Git clone, JDK 설치, Gradle build, jar(Application) 실행** `스크립트`를 여기에 입력할 수 있다.

이렇게 하면, **`EC2 인스턴스가 실행`될 때 `자동으로 서버를 실행`할 수 있다.**

```shell
#!/bin/bash

#패키지 업데이트
sudo yum update -y

#Java, Git 설치
sudo yum install -y java-17-amazon-corretto-devel
sudo yum install -y git

#Git 레포지토리 클론 및 브랜치로 이동
git clone -b 2_monolithic_cloud https://github.com/burger-2023/aws-operation-prac.git

#폴더 이동
cd aws-operation-prac

#Gradle을 이용한 Spring Boot 프로젝트 빌드 후 빌드된 Spring Boot 애플리케이션 실행
./gradlew build
sudo java -jar build/libs/aws-msa-monolithic-prac-0.1.jar
```

<br/>

<br/>

<br/>


## ⚡️ Bastion Host 로 Private EC2 Instance 에 접속

<img width="400" alt="스크린샷 2024-10-22 오후 11 18 25" src="https://github.com/user-attachments/assets/7fbb226d-4b58-4abb-9787-978a47524360">

1. Bastion Host 접속은 AWS EC2 Instance Connect 를 사용한다.
2. 서버 Instance(Private Subnet 에 배치된) 접속에 사용할 Key Pair 를 Bastion Host 로 가져와야 한다.
3. Cloud Shell 에 복사해놓은 PEM키를 복사한다.
4. 명령어 실행 후에 나온 내용을 복사한다.
5. `Bastion Host terminal` 에서 `vi` 로 파일 생성 후에, 내용을 붙여 넣은 후에 `:wq` 로 저장한다.
6. `sudo ssh -i goopang-key-pair.pem ec2-user@10.0.1.101`
7. 서버에 `User Data` 로 입력한 스크립트가 잘 실행되었는지 `CURL` 명령어를 통해 확인한다.
8. `curl -XGET 'localhost/health_check'`
9. 테스트가 정상적으로 작동하지 않는다면 `cat /var/log/cloud-init-output.log` 로 EC2 인스턴스의 User Data 스크립트 실행 로그를 확인할 수 있다.

```shell
ls -al
cat goopang-key-pair.pem
```

```shell
vi goopang-key-pair.pem
```

<br/>

<br/>

<br/>

## ⚡️ EC2 Instance Connect Endpoint

> `EC2 Instance Connect Endpoint` 를 이용하면, Private IP 만 가진 인스턴스로도 접속할 수 있다.

- 접속한 사용자의 기록이 모두 남게 되고, 
- 접속 제한 시간을 둘 수 있어 보안적으로도 강화할 수 있다.

<img width="500" alt="스크린샷 2024-10-23 오전 3 12 00" src="https://github.com/user-attachments/assets/da01d2b6-5cc4-4bc5-8d7f-d7a5140feaf2">

- `Max tunnel duration`: 해당 인스턴스로 SSH 연결을 유지할 시간, 초 단위, 해당 시간이 초과되면 연결이 자동으로 끊긴다.
- `EC2 Instance Connect Endpoint 설정`: 생성된 EC2 Instance Connect Endpoint 를 통해 Private Subnet 을 EC2 로 연결하게 된다.
  - Create Endpoint 로 Endpoint 를 새로 생성해준다.

<img width="1000" alt="스크린샷 2024-10-23 오전 3 03 10" src="https://github.com/user-attachments/assets/e2c57501-b5c0-46ce-af06-9581f3874ee7">

- sg 는 bastion host 로 접속하도록 bastion-host-sg 를 선택한다.
- Subnet 은 접속하려는 EC2 인스턴스가 있는 Subnet(여기서는 Private-subnet) 을 선택한다.

<br/>

<br/>

<br/>

## ⚡️ NAT Gateway vs NAT Instance

<img width="500" src="https://github.com/user-attachments/assets/b2421b9b-9ddf-4179-bce2-d388a4a6292a">

<img width="500" src="https://github.com/user-attachments/assets/cb4dc543-85f9-4016-b8dd-e222b3d4bed5">

- `NAT Gateway` 는 중단이 없고, 필요 없을 때는 삭제하는 선택지 밖에 없다.(비용이 나가는 것을 원하지 않느다면)
- `NAT Instance` 는 중단이 가능하고, 필요 없을 때 중단하거나 삭제할 수 있다.
  - `NAT Instance` 는 비용적인 장점이 크지만 운영 환경에서는 갑작스럽게 늘어나는 트래픽이나 네트워크 장애가 발생하면 관리할 포인트가 많아질 수 있기 때문에 dev, test 환경에서 사용하는 것이 좋다.

## ⚡️ NAT Instance 생성

<img width="500" alt="스크린샷 2024-10-23 오전 3 26 20" src="https://github.com/user-attachments/assets/ddf2c6c2-2296-4a12-aa97-42249805dde9">

- Public Subnet 에 NAT Instance 를 생성한다.
- Public IP 를 Enable 해준다.

<img width="500" alt="스크린샷 2024-10-23 오전 3 26 35" src="https://github.com/user-attachments/assets/5e7793ec-597c-4515-8588-9b4c59d6a376">

- sg 를 새로 생성한다.
  - `ssh`, `HTTP`, `HTTPS`, `ICMP` 를 허용해준다.
  - `ICMP` 는 Private Subnet Instance 에서 `ping` 테스트를 하기 위해 허용해준다.

---

Private Subnet 에서 외부로 트래픽이 나가는지 확인하는 용도로 Private Subnet 에 Instance 를 하나 더 생성해준다.     
인스턴스에서 Google 서버로 ping 을 보내면서 확인하면 된다.

<img width="1000" alt="스크린샷 2024-10-23 오전 3 41 31" src="https://github.com/user-attachments/assets/392df00d-6a62-4160-ae21-ac32dac16f93">

- Private Subnet 에서 생성하고, Public IP 는 disable 해준다.
- Inbound 규칙에는 `ssh` 만 허용해준다.

---

생성한 NAT Instance 에 접속한다.

### 🔋 iptables 설치 및 활성화

> **`IP Tables` 는 Linux 기반 시스템에서 네트워크 패킷을 필터링하고 네트워크 트래픽을 제외하는 방화벽 소프트웨어이다.**

`IP Tables` 를 활성화해준다.
- 이를 통해, 트래픽의 인바운드, 아웃바운드 규칙을 설정할 수 있다.

```shell
sudo yum install iptables-services -y
sudo systemctl enable iptables
sudo systemctl start iptables
```

### 🔋 NAT Instance 에서 IP forwarding 활성화 

> **`IP forwarding` 은 네트워크 인터페이스에서 들어오는 패킷을 다른 네트워크 인터페이스로 전달하는 기능이다.**

- NAT Instance 에서는 Private 네트워크의 트래픽을 받아 인터넷으로 전달하기 위해 IP forwarding 이 필요하다.

```shell
# 1. vi 편집기를 통한 구성파일 생성
sudo vi /etc/sysctl.d/custom-ip-forwarding.conf

# 2. 구성파일에 입력할 활성화 명령어
net.ipv4.ip_forward=1

# 3. 파일 저장 후, 구성 파일 적용 명령어
sudo sysctl -p /etc/sysctl.d/custom-ip-forwarding.conf
```

### 🔋  네트워크 인터페이스 이름을 확인 명령어

NAT 설정을 위해 네트워크 인터페이스 이름을 확인해야 한다.
- 네트워크 인터페이스는 컴퓨터가 네트워크에 연결될 수 있도록 하는 하드웨어 또는 소프트웨어이다. 이를 통해 컴퓨터는 데이터를 주고 받을 수 있다.

여기서는 `ens5` 이다.

<img width="500" alt="스크린샷 2024-10-23 오전 3 55 05" src="https://github.com/user-attachments/assets/b2a83208-942d-4f1a-b2fd-e62df279333c">

```shell
netstat -i
````

### 🔋 NAT 구성 명령어

```shell
sudo /sbin/iptables -t nat -A POSTROUTING -o 인터페이스이름 -j MASQUERADE
sudo /sbin/iptables -F FORWARD
sudo service iptables save
```

---

## ⚡️ NAT Instance 설정

<img width="1000" alt="스크린샷 2024-10-23 오전 3 57 55" src="https://github.com/user-attachments/assets/19563b53-fb20-4f73-baa7-d797c2c59127">

<img width="500" alt="스크린샷 2024-10-23 오전 4 01 15" src="https://github.com/user-attachments/assets/f017a0c5-882b-457f-819c-5cfd1b447341">

- `NAT Instance` -> `Networking` -> `Change Source/Destination check` 를 선택한다.
- 이 설정은 EC2 인스턴스가 자신이 아닌 다른 출발지 또는 목적지의 트래픽을 받아들이거나 전달하지 않도록 활성화 되어 있는데, 
- NAT Instance 는 네트워크 트래픽을 다른 인스턴스로 전달해야 하기 때문에 이 설정을 비활성화 해준다.
- Stop 을 체크하고 Save 해준다.

---

## ⚡️ Private ping test Instance 에서 Google 서버로 ping 테스트

Private Subnet 의 Routing Table 에 NAT Instance 로 가는 라우팅을 추가해준다.

<img width="1000" alt="스크린샷 2024-10-23 오전 4 08 13" src="https://github.com/user-attachments/assets/39307f64-22d6-414e-b9d8-6abd6fcf8378">

- 인터넷으로 가는 경로의 Target 을 Instance 로 설정하고,
- NAT Instance 를 선택해준다.

ping test instance 에 접속해서 아래 명령어로 테스트해본다.

```shell
ping 8.8.8.8
```

<img width="300" alt="스크린샷 2024-10-23 오전 4 12 55" src="https://github.com/user-attachments/assets/b20359cb-3280-43c7-b148-a489392e581b">

<br/>

<br/>

<br/>

## ⚡️ 정리

<img width="400" alt="스크린샷 2024-10-23 오전 4 14 41" src="https://github.com/user-attachments/assets/923c7929-86b9-452b-b89e-4cc117e72397">

- 서버로 직접적인 접근을 막는 역할
- 보안 강화를 위한 외부 접근을 허용하는 단일 지점
- 서버를 프라이빗 서브넷으로 이동
- EC2 Instance Connect Endpoint
  - 프라이빗 서브넷의 인스턴스에 접근할 수 있게 해주는 역할
  - 프라이빗 서브넷에 배치된 구팡 서비스로 배스쳔 호스트 없이 접속

개선이 필요한 부분
- 프라이빗 서브넷으로 이동한 EC2 는 외부에서 HTTP 요청을 할 수 없게 되었다.
- 외부에서 요청은 internet gateway 를 통해서 들어왔지만, Private Subnet 의 라우팅 테이블에는 NAT Gateway 로 라우팅이 되어 있기 때문이다.
- 들어오려는 트래픽은 라우팅 테이블에서 막히게 될 것이다.

<img width="400" alt="스크린샷 2024-10-23 오전 4 18 40" src="https://github.com/user-attachments/assets/ce3c1208-94a2-4af6-b67e-6f1f50167332">
