import axios from 'axios';
import customAxios from './../api/axiosInstance';

import { useEffect, useState } from 'react';
import { Alert, Button, Col, Container, Form, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { API_BASE_URL } from "../config/config";
// useParams 훅은 url에 들어 있는 동적 파라미터의 값을 챙길때 사용합니다.
import { useParams } from "react-router-dom";
import type { User } from '../types/User';
/*
상품 수정 페이지입니다.

상품 등록과 다른 점
    기본 키인 상품의 id 정보가 넘어 옵니다.
    id를 사용하여 기존에 입력했던 상품에 대한 정보를 미리 보여 주어야 합니다.(useEffect 훅 사용)

step 01 : 
기존 `상품 등록` 폼 양식을 복사합니다.

기존 상품 정보 읽기
    get 방식을 사용하여 해당 상품의 정보를 읽어 옵니다.

테스트 시나리오
    특정 상품에 대하여 '수정' 버튼을 클릭하면, 이전 상품 정보들이 입력란에 보여야 합니다.

다음 함수들은 '상품 등록'과 동일합니다.
    ControlChange 함수   
    FileSelect 함수  

ControlChange 함수
    각 컨트롤에 대한 change 이벤트 함수를 구현합니다.
    컨트롤(input type) : 이름, 가격, 재고, 상품 설명
    컨트롤(combo type) : 카테고리

FileSelect 함수    
    업로드할 이미지 선택에 대한 이벤트 함수를 구현합니다.
    FileReader API를 사용하여 해당 이미지를 Base64 인코딩 문자열로 변환 작업을 합니다.

SubmitAction 함수
    '등록'이라는 문구를 모두 '수정'으로 변경합니다.
    'insert' → 'update/${id}'로 변경합니다.
    'axios.post' → 'axios.put'으로 변경합니다.
    컨트롤에 입력된 내용들을 BackEnd로 전송합니다. 

파일 업로드시 유의 사항
    전송 방식은 post로 전송합니다.
    input 양식의 type="file"로 작성해야 합니다.

테스트 시나리오
    이미지 폴더에 "product_"로 시작하는 새로운 이미지가 업로드 되어야 합니다.    
    데이터 베이스에 이전 행의 정보가 수정이 되어야 합니다.
    상품 목록 페이지에 수정된 정보가 보여야 합니다.
    과거에 업로드했던 이전 이미지를 삭제되어야 합니다.   
*/

interface AppProps {
    user: User | null
}

function App({ user }: AppProps) {
    const { id } = useParams();
    console.log(`수정할 상품 번호 : ${id}`);

    const comment = '상품 수정';

    const initial_value = {
        name: '', price: '', category: '-', stock: '', image: '', description: ''
    }; // 상품 객체 정보

    // product는 백엔드에게 넘겨줄 상품 등록 정보를 담고 있는 객체
    const [product, setProduct] = useState(initial_value);

    // 오류 메시지 상태값 (서버 검증 오류용)
    const initialErrors = {
        name: '',
        price: '',
        category: -'',
        stock: '',
        image: '',
        description: '',
        general: ''
    };

    // 오류 메시지 상태값 (서버 검증 오류용)
    const [errors, setErrors] = useState(initialErrors);

    const navigate = useNavigate();

    // id를 이용하여 기존에 입력한 상품 정보 가져오기
    useEffect(() => {
        if (!user || user.role !== 'ADMIN') {
            alert(`${comment} 기능은(는) 관리자만 접근이 가능합니다.`);
            navigate('/');
        }

        const url = `${API_BASE_URL}/product/update/${id}`;

        customAxios
            .get(url)
            .then((response) => {
                setProduct(response.data);
            })
            .catch((error) => {
                console.log(`상품 ${id}번 오류 발생 : ${error}`);
                alert('해당 상품 정보를 읽어 오지 못했습니다.');
            });

    }, [id, user, navigate]); // id 값이 변경될 때 마다 화면을 re-rendering 시켜야 합니다.


    // 폼 양식에서 어떠한 컨트롤의 값이 변경되었습니다.
    const ControlChange = (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
    ) => {
        // event 객체는 change 이벤트를 발생시킨 폼 컨트롤입니다.
        const { name, value } = event.target;
        //console.log(`값이 바뀐 컨트롤 : ${name}, 값 : ${value}`);

        // 전개 연산자를 사용하여 이전 컨트롤의 값들도 보존하도록 합니다.
        setProduct({ ...product, [name]: value });
    }

    const FileSelect = (
        event: React.ChangeEvent<HTMLInputElement>
    ) => {
        // 자바 스크립트는 모든 항목들을 배열로 취급하는 성질을 가지고 있습니다.
        const { name, files } = event.target;


        // null 및 길이 체크
        if (!files || files.length === 0) {
            alert('이미지 파일을 선택해 주셔야 합니다.');
            return;
        }
        const file = files[0]; // type="file"로 작성한 1번째 항목

        // FileReader는 웹 브라우저에서 제공해주는 내장 객체로, 파일 읽기에 사용 가능합니다.
        // 자바 스크립트에서 파일을 읽고 이를 데이터로 처리하는 데 사용됩니다.
        const reader = new FileReader();

        // readAsDataURL() 함수는 file 객체를 문자열 형태(Base64 인코딩)로 반환하는 역할을 합니다.
        reader.readAsDataURL(file);

        // onloadend : 읽기 작업이 성공하면 자동으로 동작하는 이벤트 핸들러 함수
        reader.onloadend = () => {
            const result = reader.result;
            //console.log(result);

            // 해당 이미지는 Base64 인코딩 문자열 형식으로 state에 저장합니다.
            // 사용 예시 : data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA...
            // [name]는 name 변수의 값, 즉 image의 값을 바꾸겠습니다.
            // name 이라고 적으면, 무조건 상품 이름(name)이 계속 변경됩니다.				
            setProduct({ ...product, [name]: result });
        };
    };


    const SubmitAction = async (event: React.SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (product.category === "-") {
            alert('상품 카테고리는 반드시 선택해 주셔야 합니다.');
            return; // 수정 중단
        }

        try {
            // 주의) 라우팅 규칙 때문에 ${id}를 제거하면 안됩니다.
            const url = `${API_BASE_URL}/product/update/${id}`;


            // Content-Type(Mime Type) : 문서의 종류가 어떠한 종류인지를 알려 주는 항목
            // 예시 : 'text/html', 'image/jpeg', 'application/json' 등등
            // 이 문서는 json 형식의 파일입니다.            
            const config = {
                headers: { 'Content-Type': 'application/json' }
            };
            // put() 메소드는 리소스를 "수정"하고자 할 때 사용하는 메소드입니다.
            const response = await axios.put(url, product, config);

            console.log(`상품 수정 : [${response.data}]`);
            alert('상품이 성공적으로 수정 되었습니다.');

            // 상품 등록후 입력 컨트롤은 모두 초기화 되어야 합니다.
            setProduct(initial_value);

            setErrors(initialErrors); // 오류 초기화 

            // 등록이 이루어 지고 난 후 상품 목록 페이지로 이동합니다.
            navigate('/product/list');

        } catch (error: unknown) {
            if (axios.isAxiosError(error) && error.response) {
                // 백엔드에서 전달받은 오류 메시지를 저장
                setErrors((prev) => ({
                    ...prev,
                    ...error.response?.data?.errors,
                    general: error.response?.data?.message || '상품 수정 중 오류가 발생했습니다.'
                }));
            } else {
                setErrors((prev) => ({
                    ...prev,
                    general: '서버와의 통신 중 오류가 발생했습니다.'
                }));
            }
        };
    };

    return (
        <Container style={{ marginTop: '30px' }}>
            <h1>{comment}</h1>

            {/* 일반 오류 메시지 */}
            {errors.general && <Alert variant="danger">{errors.general}</Alert>}
            <Form onSubmit={SubmitAction}>

                {/* 이름 */}
                <Form.Group as={Row} className="mb-3" controlId="formName">
                    <Form.Label column sm={2}>이름</Form.Label>
                    <Col sm={10}>
                        <Form.Control
                            type="text"
                            placeholder="이름을(를) 입력해 주세요."
                            name="name"
                            value={product.name || ''}
                            onChange={ControlChange}
                            isInvalid={!!errors.name}
                        />
                        <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                    </Col>
                </Form.Group>

                {/* 가격 */}
                <Form.Group as={Row} className="mb-3" controlId="formPrice">
                    <Form.Label column sm={2}>가격</Form.Label>
                    <Col sm={10}>
                        <Form.Control
                            type="text"
                            placeholder="가격을(를) 입력해 주세요."
                            name="price"
                            value={product.price || ''}
                            onChange={ControlChange}
                            isInvalid={!!errors.price}
                        />
                        <Form.Control.Feedback type="invalid">{errors.price}</Form.Control.Feedback>
                    </Col>
                </Form.Group>

                {/* 카테고리 */}
                <Form.Group as={Row} className="mb-3" controlId="formCategory">
                    <Form.Label column sm={2}>카테고리</Form.Label>
                    <Col sm={10}>
                        <Form.Select
                            name="category"
                            value={product.category || ''}
                            onChange={ControlChange}
                            isInvalid={!!errors.category}
                        >
                            <option value="-">-- 상품 카테고리를 선택해 주세요.</option>
                            <option value="BREAD">빵</option>
                            <option value="BEVERAGE">음료수</option>
                            <option value="CAKE">케이크</option>
                            <option value="MACARON">마카롱</option>
                        </Form.Select>
                        <Form.Control.Feedback type="invalid">{errors.category}</Form.Control.Feedback>
                    </Col>
                </Form.Group>

                {/* 재고 */}
                <Form.Group as={Row} className="mb-3" controlId="formStock">
                    <Form.Label column sm={2}>재고</Form.Label>
                    <Col sm={10}>
                        <Form.Control
                            type="text"
                            placeholder="재고을(를) 입력해 주세요."
                            name="stock"
                            value={product.stock || ''}
                            onChange={ControlChange}
                            isInvalid={!!errors.stock}
                        />
                        <Form.Control.Feedback type="invalid">{errors.stock}</Form.Control.Feedback>
                    </Col>
                </Form.Group>

                {/* 이미지 */}
                <Form.Group as={Row} className="mb-3" controlId="formImage">
                    <Form.Label column sm={2}>이미지</Form.Label>
                    <Col sm={10}>
                        <Form.Control
                            type="file"
                            name="image"
                            onChange={FileSelect}
                            isInvalid={!!errors.image}
                        />
                        <Form.Control.Feedback type="invalid">{errors.image}</Form.Control.Feedback>
                    </Col>
                </Form.Group>

                {/* 상품 설명 */}
                <Form.Group as={Row} className="mb-3" controlId="formDescription">
                    <Form.Label column sm={2}>상품 설명</Form.Label>
                    <Col sm={10}>
                        <Form.Control
                            type="text"
                            placeholder="상품 설명을(를) 입력해 주세요."
                            name="description"
                            value={product.description || ''}
                            onChange={ControlChange}
                            isInvalid={!!errors.description}
                        />
                        <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                    </Col>
                </Form.Group>

                <Button variant='primary' type='submit' size='lg'>{comment}</Button>
            </Form>
        </Container >
    );
}

export default App;