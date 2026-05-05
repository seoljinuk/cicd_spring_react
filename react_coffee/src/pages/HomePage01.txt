import { Carousel, Container } from "react-bootstrap";
import { API_BASE_URL } from "../config/config";

function App() {
    return (
        <Container className="mt-4">
            <Carousel>
                <Carousel.Item>
                    <img
                        className="d-block w-100"
                        src={`${API_BASE_URL}/images/croissant_03_bigsize.png`}
                        alt="크로아상"
                    />
                    <Carousel.Caption>
                        <h3>크로아상</h3>
                        <p>바삭하고 결이 살아있는 프랑스식 버터 페이스트리</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img
                        className="d-block w-100"
                        src={`${API_BASE_URL}/images/brioche_04_bigsize.png`}
                        alt="브리오슈"
                    />
                    <Carousel.Caption>
                        <h3>브리오슈</h3>
                        <p>달콤하고 부드러운 식감의 버터 함유 빵.</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img
                        className="d-block w-100"
                        src={`${API_BASE_URL}/images/americano01_bigsize.png`}
                        alt="아메리카노"
                    />
                    <Carousel.Caption>
                        <h3>아메리카노</h3>
                        <p>에스프레소에 뜨거운 물을 추가한 커피.</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img
                        className="d-block w-100"
                        src={`${API_BASE_URL}/images/whitewine01_bigsize.png`}
                        alt="화이트 와인"
                    />
                    <Carousel.Caption>
                        <h3>화이트 와인</h3>
                        <p>청포도로 만든 가볍고 산뜻한 와인.</p>
                    </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                    <img
                        className="d-block w-100"
                        src={`${API_BASE_URL}/images/french_baguette_01_bigsize.png`}
                        alt="프랑스 바게트"
                    />
                    <Carousel.Caption>
                        <h3>프랑스 바게트</h3>
                        <p>바삭한 겉과 쫄깃한 속의 긴 막대형 빵.</p>
                    </Carousel.Caption>
                </Carousel.Item>
            </Carousel>
        </Container>
    );
}

export default App;