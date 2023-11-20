import { configureStore } from "@reduxjs/toolkit";
import CartReducer from "../reducer/CartReducer";

export default configureStore({
    reducer:{
        cart:CartReducer
    }
})
