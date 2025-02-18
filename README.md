참고할 주소
1강부터 11강까지의 소스코드
(자바11, 스프링부트 버전은 3.0 미만이어야 합니다)
https://github.com/codingspecialist/-Springboot-Security-OAuth2.0-V3


18강부터 27강까지 소스코드
(자바11, 스프링부트 버전은 3.0 미만이어야 합니다)
https://github.com/codingspecialist/Springboot-Security-JWT-Easy/tree/version2


#  mvc:
#    view:
#      prefix: /templates/
#      suffix: .mustache

이거는 mustache라이브러리가 알아서 적용해준다 그러므로 yml 파일에 작성할 필요가 없다 


<html lang="kr">
<head>
    <meta charset="UTF-8">
    <title>로그인 페이지</title>
</head>
<body>

</body>
</html>

### EnableMethodSecurity 와 EnableGlobalMethodSecurity 는 최신이냐 구버전이냐 차이다 최신 버전에서 사용하는 EnableMethodSecurity 이걸 사용하면 된다 
@EnableMethodSecurity(securedEnabled = true) //@EnableMethodSecurity는 최신 Spring Security에서 @EnableGlobalMethodSecurity를 대체할 수 있습니다. 
두 애노테이션 모두 메서드 수준의 보안을 활성화하는 기능을 제공하지만, @EnableMethodSecurity는 최신 버전에서 권장되는 방식

### oauth 로그인  
1. goole api console 접속 로그인 api 들어가기 
2. api 새로 생성
3. oauth 동의 화면 이동 내부, 외부 에서 외부 선택 
4. 애플리케이션 이름 작성후 저장 
5. 사용자 인증 정보 만들기  
6. oauth 클라이언트 id 클릭
7. 애플리케이션 유형은 웹 애플리케이션 선택 
8. 이름 작성 
9. 승인된 리디렉션 URI 작성 http://localhost:8080/login/oauth2/code/google 작성 = 구글 로그인이 완료가 되면 구글 서버에서 코드를 우리쪽으로 코드(인증이 된 코드)를 돌려준다 코드를 받아서 accesstoken을 요청한다
   accesstoken을 받아서 사용자 대신 우리 서버가 구글 서버를 사용자에 개인정보나 민감한 정보를 접근할 수 있는 권한이 생긴다 그리고 이런 코드를 받을 주소를 작성한게 http://localhost:8080/login/oauth2/code/google 이거이다
10. oauth 라이브러리를 사용하면 http://localhost:8080/login/oauth2/code/google 이주소는 고정이다  http://localhost:8080 여기까지는 자유롭게 작성가능하다 /login/oauth2/code 는고정이다(중요)  google이부분은 내가 네이버를 naver 이렇게 작성하면 된다
11. http://localhost:8080/login/oauth2/code/google 이거에 대한 컨트롤러 주소는 만들 필요없다 이거는 우리가 제어할게 아니라 라이브러리가 알아서 처리해준다
12. <a href="/oauth2/authorization/google" >구글 로그인 </a> <!--/oauth2/authorization/ 이주소도 oauth라이브러리에 있는거기 때문에 내 마음대로 주소를 변경 못한다  -->

security:
oauth2:
client:
registration:
google:
client-id: ${googleId} //사용자 인증 정보에 id가 있다
client-secret: ${googlePw} //사용자 인증 정보에 pw가 있다
scope:
- email
- profile