import {createSlice} from '@reduxjs/toolkit';
import Toast from 'react-native-toast-message';

export const cartSlice = createSlice({
    name:'cart',
    initialState:{
    },
    reducers:{
        setTableNumber: (state,action) => {
            state.tableNumber = action.payload;
        },
        addToCart : async (state, action) => {
            const response = await fetch('http://192.168.1.2:8080/order/addToOrder?dishId='+action.payload.id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(state.cart)
            });

            state.cart.push(response.body);

            Toast.show({
                type: 'success',
                text1: 'Dodano do koszyka',
                text2: itemToast.name + ' x' + itemToast.quantity
            });
        },
        removeFromCart :async (state, action) => {
            const response = await fetch('http://192.168.1.2:8080/order/removeFromOrder?dishId=' + action.payload.id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(state.cart)
            });

            state.cart.push(response.body);
        },
        clearCart: (state,action) => {
            state.cart = [];
            state.tableNumber = '';
        }
    }
})


export const {addToCart,removeFromCart,incrementQty,decrementQty, clearCart, setTableNumber} = cartSlice.actions;

export default cartSlice.reducer;
