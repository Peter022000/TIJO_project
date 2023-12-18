import CartReducer from "../reducer/CartReducer";
import { configureStore } from '@reduxjs/toolkit';
import thunk from 'redux-thunk';

const store = configureStore({
    reducer: {
        cart: CartReducer,
    },
    middleware: [thunk],
});

export default store;
