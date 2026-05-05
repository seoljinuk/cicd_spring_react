import axios from "axios"

import { useEffect, useState } from "react";
import { API_BASE_URL } from "../config/config";
import { Table } from "react-bootstrap";

import type { Fruit } from "../types/Fruit"

// axios 라이브러리를 이용하여 리액트에서 스프링으로 데이터를 요청해야 합니다.
function App() {
    // Fruit 타입으로 상태를 지정하세요.
    // 처음에는 값이 없으므로 null, 데이터가 들어 오면 Fruit 타입
    // const [state변수, state변경함수] = useState<타입>(초기값);
    const [fruit, setFruit] = useState<Fruit | null>(null); // 넘겨 받은 과일 1개

    useEffect(() => { // BackEnd 서버에서 데이터 읽어 오기
        const fetchFruit = async () => {
            try {
                const url = `${API_BASE_URL}/fruit`; // 요청할 url 주소
                const response = await axios.get<Fruit>(url, {
                    withCredentials: true,
                });
                setFruit(response.data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchFruit();
    }, []);

    return (
        <>
            <Table hover style={{ margin: '20px' }}>
                <tbody>
                    <tr>
                        <td>아이디</td>

                        {/* optional chaining은 객체가 null 또는 undefined일 때 오류 없이 접근하도록 하는 자바 스크립트 문법입니다.*/}
                        {/* optional chaining : fruit가 null → 아무것도 안 나옴(undefined 반환), fruit가 존재 → id 출력 */}
                        <td>{fruit?.id}</td>
                    </tr>
                    <tr>
                        <td>상품명</td>
                        <td>{fruit?.name}</td>
                    </tr>
                    <tr>
                        <td>단가</td>
                        <td>{fruit?.price.toLocaleString()}원</td>
                    </tr>
                </tbody>
            </Table >
        </>
    );
}

export default App;
