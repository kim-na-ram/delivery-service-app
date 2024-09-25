## 🏍️ 세기말 배달
배달 어플리케이션 개발에 관련된 아웃소싱 프로젝트입니다
<br/><br />

## ⭐ TEAM - 21세기 히트조
#### [👤 mylotto0626](https://github.com/mylotto0626)
#### [👤 jiyumi00](https://github.com/jiyumi00)
#### [👤 kim-na-ram](https://github.com/kim-na-ram)
#### [👤 Ahn-donghwan](https://github.com/Ahn-donghwan)
<br/><br />

## ⚙️ 프로젝트 설계
### 와이어 프레임 
#### [피그마 링크](https://www.figma.com/board/Fo0kF0xJZH1dkS87RF3ho3/Untitled?node-id=0-1&node-type=canvas)
<br/></br>

### API 명세서
![image](https://github.com/user-attachments/assets/ad1831b9-bfa5-4f4a-a213-4846c97b7c8b)
![image](https://github.com/user-attachments/assets/f3433b1c-e8b1-4cac-b027-e743a205f76d)
![image](https://github.com/user-attachments/assets/e11b22a1-b709-4b88-a1d6-cc0c9687ac4a)
![image](https://github.com/user-attachments/assets/ab7a0fc7-ab30-48e0-9961-da718bc4a834)
![image](https://github.com/user-attachments/assets/46c61750-ebc8-4b74-8e00-f0b52a779300)
![image](https://github.com/user-attachments/assets/e454bc02-36c2-46c7-a1d9-34639bebe58b)
<br/></br>

### ERD diagram
![image](https://github.com/user-attachments/assets/c35290b7-3bab-402e-a1d5-6fa332066a5a)
<br/></br>

## 📌 프로젝트 기능 정리
### 회원
회원가입 및 로그인

    - 이메일, 비밀번호, 닉네임, 사용자 권한을 입력해 가입할 수 있습니다.
    - 아이디는 이메일이며, 중복이 불가능합니다
    - 이메일, 비밀번호를 입력해 로그인할 수 있습니다.
    - 회원 탈퇴를 할 수 있습니다.

### 가게

가게 등록

    - 가게명, 가게소개, 오픈시간, 마감시간, 최소 주문 금액을 입력해 생성할 수 있습니다. 
    - 사장님 권한을 가진 유저만 가능하며, 최대 3개까지만 운영할 수 있습니다.
    

가게 수정

    - 가게명, 가게소개, 오픈시간, 마감시간, 최소 주문 금액을 입력해 수정할 수 있습니다


가게 조회

    - 가게명으로 가게 리스트를 조회할 수 있습니다.
    - 단건 조회 시, 메뉴 목록을 볼 수 있습니다.
    
가게 폐업

    - 폐업 시, 가게의 상태만 폐업상태로 변경됩니다.

### 메뉴

메뉴 생성/수정

    - 사장님 권한 유저만 메뉴 생성 및 수정할 수 있습니다.

메뉴 삭제

    - 삭제 시, 메뉴의 상태만 삭제 상태로 변경됩니다.
    - 가게 메뉴 조회 시, 삭제된 메뉴는 나타나지 않습니다.

### 주문

주문 생성

    - 유저는 각 주문에 하나의 메뉴만 주문할 수 있습니다.
    
주문 상태 변경

    - 사장님만 변경 및 주문을 수락할 수 있습니다.
    - 배달이 완료될 때까지의 모든 상태를 순서대로 변경합니다.
    - 사장님은 주문을 수락하기 전에만 주문 취소가 가능합니다.

### 리뷰
  
리뷰 생성

    - 유저는 주문 건에 대해 리뷰를 작성할 수 있습니다.
    - 리뷰는 별점을 부여할 수 있습니다 (1~5)
    - 배달이 완료되지 않은 주문은 리뷰를 작성할 수 없습니다.
    
리뷰 조회

    - 리뷰는 가게 정보를 기준으로 다건 조회 가능하며, 최신순으로 정렬합니다
    - 리뷰를 별점 범위에 따라 조회할 수 있습니다.
    
리뷰 삭제

    - 작성한 유저, 가게 사장님만 삭제가 가능합니다.
   

<br/><br />
## 🚀 STACK

Environment


![인텔리제이](   https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![](https://img.shields.io/badge/Gradle-02303a?style=for-the-badge&logo=gradle&logoColor=white)
![](https://img.shields.io/badge/Postman-ff6c37?style=for-the-badge&logo=postman&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![깃](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)

Development

![스프링부트](https://img.shields.io/badge/SpringBoot-6db33f?style=for-the-badge&logo=springboot&logoColor=white)
![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=Redis&logoColor=white)

Communication

![슬랙](  https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![피그](https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)

<br/><br />



<br/><br />

