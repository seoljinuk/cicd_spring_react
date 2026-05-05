export interface CartProduct {
  cartProductId: number;
  productId: number;
  image: string;
  name: string;
  quantity: number;
  price: number;
  stock: number;
  checked: boolean;
};