# 프로젝트 개요
내일배움캠프에서 진행하는 한달인턴 안드로이드 트랙 개인과제

## 과제 내용

다음 기능을 갖춘 프로젝트를 만들어가며, 실제 업무에 필요한 필수 역량을 갖추게 될 거에요.(아래 기능을 기반으로 자유롭게 구현해주세요)

1. **회원가입**
    1. 직접 회원가입 페이지를 만들어봅니다.
    2. 모습은 어떤 것이든 상관없습니다. 본인이 가장 자랑스럽고 재미있게 만들어보세요.
2. **로그인**
    1. 직접 로그인 페이지를 만들어봅니다.
    2. 모습은 어떤 것이든 상관없습니다. 본인이 가장 자랑스럽고 재미있게 만들어보세요.
3. **테스트하기**
    1. 회원가입/로그인 페이지 완성후 완벽한지 테스트하기
        - 다양한 상황에서 테스트해보기
      
## 기술 스택
|종류|상세|
|---|---|
|아키텍쳐|Clean Architecture|
|디자인패턴|MVVM, Repository, DI(Dagger Hilt)|
|비동기처리|Coroutine, Flow|
|DB|Firebase FireStore|
|인증시스템|Firebase Authentication(+Google Login)|
|UI|Material3, Viewpager2, Fragment|

## 화면
실행 시
|로그인 되어 있으면|로그인 되어 있지 않으면|
|:---:|:---:|
|<img src = "https://github.com/user-attachments/assets/2562ad06-7b81-4729-b3cf-a68e22c7f584" width = "150" height = "320"/>|<img src = "https://github.com/user-attachments/assets/7ddc4a32-2e39-47fa-b6bc-008c6d5a39ec" width = "150" height = "320"/>|

이메일 버튼을 눌렀을 때
|이메일로 회원정보 확인|
|:---:|
|<img src = "https://github.com/user-attachments/assets/d4afdfad-d519-4ce8-ba12-2e24587b5116" width = "150" height = "320"/>|

다음버튼을 눌렀을 때
|회원정보가 있으면|회원정보가 없으면|
|:---:|:---:|
|<img src = "https://github.com/user-attachments/assets/a721116c-d076-4ac7-9bb7-de39bc4634fa" width = "150" height = "320"/>|<img src = "https://github.com/user-attachments/assets/5fa0b616-31dc-4836-bb90-97a4a8ff2731" width = "150" height = "320"/>|

회원정보가 확인되면
|비밀번호 입력 후 로그인|
|:---:|
|<img src = "https://github.com/user-attachments/assets/0b089e31-2e5c-47e4-953f-39f43ff06aff" width = "150" height = "320"/>|

회원정보가 없으면
|비밀번호 입력|이름 입력|전화번호 입력 후 회원가입|
|:---:|:---:|:---:|
|<img src = "https://github.com/user-attachments/assets/61cf4b56-8b97-4e7b-8579-bbb9ebd6aa8c" width = "150" height = "320"/>|<img src = "https://github.com/user-attachments/assets/4c3a3367-7c26-4f1b-9e32-0567d9a3ccb0" width = "150" height = "320"/>|<img src = "https://github.com/user-attachments/assets/f7fa7044-eb6b-4808-bb1a-aa19ef2e08d5" width = "150" height = "320"/>|

정보 확인버튼을 눌렀을 때
|서버에 저장된 정보 확인 다이얼로그|
|:---:|
|<img src = "https://github.com/user-attachments/assets/7ea38495-87b9-499d-8e13-70c9beff7cb7" width = "150" height = "320"/>|

## 트러블슈팅
### 구글 로그인 시 DB에 정보가 생성되지 않는 문제
#### 문제 분석
구글 로그인 시 기존 사용하던 회원가입 메서드가 아닌 새로 작성한 메서드를 사용했기 때문에 정보를 수집할 수 없었고 DB에 정보가 저장되지도 않는 문제
#### 시도
1. 구글 로그인 시 구글계정에서 제공받는 정보를 DB에 저장하는 로직을 구성함 -> 정보는 받아올 수 있었으나 로그인 할 때마다 구글계정의 정보로 다시 갱신됨
2. 회원가입 시점에 해당 정보를 받아와서 저장하는 방법을 고려 -> 구글로그인은 파이어베이스에서 제공하는 GoogleSignInClient로 토큰을 받아오기 때문에 해당 시점까지 Firebase Auth상에 로그인 정보가 없음
#### 해결
최초 로그인인지 확인하는 새로운 메서드를 작성하여 최초로그인시에만 DB에 정보를 갱신하도록 설정하여 해결
#### 인사이트
파이어베이스로 구글 로그인을 구현할 때 DB에 정보를 저장하는 로직이 추가적으로 필요하다는 것을 알게됨
