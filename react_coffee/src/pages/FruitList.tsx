import axios from "axios";

import { useEffect, useState } from "react";
import { Table } from "react-bootstrap";
import { API_BASE_URL } from "../config/config";
import type { Fruit } from "../types/Fruit"

function App() {
    // <Fruit[]>는 Fruit 객체들의 배열, ([])는 초기값이 빈 배열이라는 뜻입니다.
    const [fruitList, setFruitList] = useState<Fruit[]>([]); // 넘어온 과일 목록
    const url = `${API_BASE_URL}/fruit/list`;

    useEffect(() => {
        const fetchData = async () => {
            try { // axios에 제네릭 타입 추가
                const response = await axios.get<Fruit[]>(url);
                setFruitList(response.data);
            } catch (error) {
                console.error(error);
            }
        };

        fetchData();
    }, []);

    return (
        <>
            <Table hover style={{ margin: '20px' }}>
                <thead>
                    <tr>
                        <th>아이디</th>
                        <th>상품명</th>
                        <th>단가</th>
                    </tr>
                </thead>
                <tbody>
                    {fruitList.map((fruit) =>
                        <tr key={fruit.id}>
                            <td>{fruit.id}</td>
                            <td>{fruit.name}</td>
                            <td>{fruit.price.toLocaleString()} 원</td>
                        </tr>
                    )}
                </tbody>
            </Table >
        </>
    );
}

export default App;
