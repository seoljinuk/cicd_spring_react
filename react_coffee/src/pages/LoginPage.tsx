import { useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

import axios from "../api/axiosInstance";
import type { LoginResponse, User } from "../types/User";

interface Props {
    onLogin: (user: User) => void;
}

function Login({ onLogin }: Props) {

    // 로그인 state
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // 에러 메시지
    const [errors, setErrors] = useState("");

    const navigate = useNavigate();

    const handleLogin = async (event: React.SubmitEvent) => {
        event.preventDefault();
        console.log("로그인 시도 중...");

        try { // email : 파라미터로 넘겨 지는 로그인 사용자가 기입한 이메일
            // URLSearchParams = 데이터를 “key=value” 형태로 변환해주는 객체
            // URLSearchParams는 form-urlencoded 방식이므로 JSON 방식으로 보낼때 사용하면 안됨
            const params = {
                email,
                password
            };
            const response = await axios.post<LoginResponse>(
                "/member/login",
                params, {
                headers: {
                    "Content-Type": "application/json"
                }
            });

            // 서버에서 내려 받은 데이터 챙기기            
            console.log("응답 데이터 : \n", response.data);

            // 서버 응답 → User로 "변환(mapping)" 해야 합니다
            const { accessToken, ...userData } = response.data;

            // JWT 저장
            // localStorage에서 확인하는 방법은 Application Tab에서 'Local storage' 항목 참조
            localStorage.setItem("accessToken", accessToken);

            if (onLogin) {
                onLogin(userData);
                localStorage.setItem("user", JSON.stringify(userData));
            }

            console.log("로그인 성공 사용자:", userData);

            // ✅ 홈으로 이동
            navigate("/");

        } catch (error: any) {
            console.log('aa');
            if (error.response) {
                console.log('bb');
                setErrors(error.response.data.message || "로그인 실패");
            } else {
                console.log('cc');
                setErrors("Server Error");
            }
        }
    };

    return (
        <Container fluid className="d-flex justify-content-center align-items-center" style={{ height: "70vh" }}>
            <Row className="w-100 justify-content-center">
                <Col md={6} sm={10}>
                    <Card>
                        <Card.Body>
                            <h2 className="text-center mb-4">로그인</h2>

                            {errors && <Alert variant="danger">{errors}</Alert>}

                            <Form onSubmit={handleLogin}>
                                <Form.Group as={Row} className="mb-3 align-items-center">
                                    <Form.Label column sm={3} className="text-end fw-bold text-primary">
                                        이메일
                                    </Form.Label>
                                    <Col sm={9}>
                                        <Form.Control
                                            type="email"
                                            placeholder="이메일을 입력해 주세요."
                                            value={email}
                                            onChange={(e) => setEmail(e.target.value)}
                                            required
                                        />
                                    </Col>
                                </Form.Group>

                                <Form.Group as={Row} className="mb-3 align-items-center">
                                    <Form.Label column sm={3} className="text-end fw-bold text-primary">
                                        비밀 번호
                                    </Form.Label>
                                    <Col sm={9}>
                                        <Form.Control
                                            type="password"
                                            placeholder="비밀 번호를 입력해 주세요."
                                            value={password}
                                            onChange={(e) => setPassword(e.target.value)}
                                            required
                                        />
                                    </Col>
                                </Form.Group>

                                <Row className="g-2">
                                    <Col xs={8}>
                                        <Button variant="primary" type="submit" className="w-100">
                                            로그인
                                        </Button>
                                    </Col>
                                    <Col xs={4}>
                                        <Link to="/member/signup" className="btn btn-outline-secondary w-100">
                                            회원 가입
                                        </Link>
                                    </Col>
                                </Row>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}

export default Login;