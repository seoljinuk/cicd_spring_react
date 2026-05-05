interface OrderItem {
    productName: string;
    quantity: number;
}

export interface Order {
    name: string;
    orderId: number;
    orderDate: string; // 날짜는 보통 string으로 받음
    status: string;
    orderItems: OrderItem[];
}