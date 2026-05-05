/*
TypeScript 타입 정의
User라는 객체는 반드시 다음 형태이어야 함을 알려 주는 타입(설계도)입니다.

이건 문자열 리터럴 유니온 타입 입니다.
role은 오직 "USER" 또는 "ADMIN"만 가능합니다.
*/
/* 리액트 앱 내부에서 사용하는 사용자 모델 */
export interface User {
    id: number;
    name: string;
    email: string;
    role: "USER" | "ADMIN";
}

/* 서버가 로그인 시 내려주는 응답 */
// LoginResponse는 User를 포함
export interface LoginResponse extends User {
    accessToken: string;
}