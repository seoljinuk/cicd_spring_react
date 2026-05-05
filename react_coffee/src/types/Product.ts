export interface Product {
    id: number;
    name: string;
    price: number;
    category: string;      // Enum은 JSON으로 오면 문자열
    stock: number;
    image: string;
    description: string;
    inputdate: string;     // LocalDate → 문자열로 넘어옴 (예: "2026-02-20")
}