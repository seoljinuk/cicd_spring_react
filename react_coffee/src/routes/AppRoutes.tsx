import { Route, Routes } from "react-router-dom";

import OrderList from '../pages/OrderList';
import CartList from './../pages/CartList';
import FruitList from './../pages/FruitList';
import FruitOne from './../pages/FruitOne'; // 표현식01
import HomePage from './../pages/HomePage';
import LoginPage from './../pages/LoginPage';
import ProductDetail from './../pages/ProductDetail';
import ProductInsertForm from './../pages/ProductInsertForm';
import ProductList from './../pages/ProductList';
import ProductUpdateForm from './../pages/ProductUpdateForm';
import SignupPage from './../pages/SignupPage';

import type { User } from "../types/User";

interface AppProps {
    user: User | null;
    handleLoginSuccess: (userData: User) => void;
}

function App({ user, handleLoginSuccess }: AppProps) {
    return (
        <Routes>
            <Route path='/fruit' element={<FruitOne />} />
            <Route path='/fruit/list' element={<FruitList />} />
            <Route path='/' element={<HomePage />} />
            <Route path='/member/signup' element={<SignupPage />} />
            <Route path='/member/login' element={< LoginPage onLogin={handleLoginSuccess} />} />

            <Route path='/product/list' element={<ProductList user={user} />} />
            <Route path='/product/insert' element={<ProductInsertForm user={user} />} />

            {/* 기호 ":id"는 변수처럼 동작하는 매개 변수이고, ProductUpdateForm.js 파일에서 참조합니다. */}
            <Route path='/product/update/:id' element={<ProductUpdateForm user={user} />} />

            <Route path='/product/detail/:id' element={<ProductDetail user={user} />} />

            <Route path='/cart/list' element={<CartList user={user} />} />

            <Route path='/order/list/' element={<OrderList user={user} />} />
        </Routes>
    );
}

export default App;