import { useEffect, useState } from "react";
import { API_BASE_URL } from "../config/config";

import { Alert, Container, Spinner } from "react-bootstrap";
import type { Order } from "../types/Order";
import type { User } from "../types/User";
import customAxios from './../api/axiosInstance';

type AppProps = {
    user: User | null; // user는 User 객체 혹은 null일 수도 있습니다.
}

function App({ user }: AppProps) {
    // loading이 true이면 현재 데이터를 읽고 있는 중입니다.
    const [loading, setLoading] = useState(true);

    // 오류 정보를 저장할 스테이트
    const [error, setError] = useState('');

    // 주문 목록들을 저장할 스테이트(초기 값 : 빈 배열)
    const [orders, setOrders] = useState<Order[]>([]);

    // 다음의 hook은 사용자 정보 user가 변경될 때 마다 rendering 됩니다.
    useEffect(() => {
        if (!user) {
            setError('로그인이 필요합니다.');
            setLoading(false);
            return;
        }

        // 스프링 부트의 OrderController의 getOrderList() 메소드 참조
        const fetchOrders = async () => {
            try {
                const url = `${API_BASE_URL}/order/list`;

                // get 방식은 파라미터를 넘길 때, params라는 키를 사용하여 넘겨야 합니다.
                const parameters = {
                    params: {
                        memberId: user?.id,
                        role: user?.role
                    }
                };
                const response = await customAxios.get(url, parameters);

                setOrders(response.data);

                console.log('get 방식 : /order/list');
                console.log(response.data);

            } catch (error) {
                setError('주문 목록을 불러 오는 데 실패하였습니다.');
                console.log(error);

            } finally {
                setLoading(false);
            };
        };

        fetchOrders(); // 함수 호출

    }, [user]);

    if (loading) {
        return (
            <div className="d-flex justify-content-center align-items-center p-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">주문 목록을 불러오는 중입니다.</span>
                </Spinner>
            </div>
        );
    }

    if (error) {
        return (
            <Container className="my-4">
                <Alert variant="danger">{error}</Alert>
            </Container>
        );
    }



    return (
        <Container className="my-4">
            <h1 className="my-4">주문 내역</h1>
            이전에 {orders.length} 개 주문하셨네요.
        </Container>
    );

}

export default App;