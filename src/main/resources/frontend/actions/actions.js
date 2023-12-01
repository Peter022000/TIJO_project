import axios from 'axios';
import Toast from 'react-native-toast-message';

export const acceptOrder = () => async (dispatch, getState) => {
    try {
        const state = getState();

        let body = JSON.stringify({
            tableNoId: state.cart.tableNoId,
            cost: state.cart.cost,
            order: state.cart.dishes,
            paymentMethod: state.cart.paymentMethod});

        console.log(body)
        const response = await axios.post('http://192.168.1.2:8080/order/acceptOrder', body, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        dispatch({
            type: 'ACCEPT_ORDER'
        });
    } catch (error) {
        Toast.show({
            type: 'error',
            text1: 'Nie udało się złożyć zamówienia',
            text2: error
        });
        console.error('Error while accepting order:', error);
    }
};

export const savePaymentMethod = (paymentMethod) => (dispatch, getState) => {
    try {
        dispatch({
            type: 'SAVE_PAYMENT_METHOD',
            payload: {
                data: paymentMethod
            }
        });
    } catch (error) {
        console.error(error);
    }
};


export const addToCart = (dishId, fromType) => async (dispatch, getState) => {
    try {
        const state = getState();

        let body = JSON.stringify({
            tableNoId: state.cart.tableNoId,
            cost: state.cart.cost,
            order: state.cart.dishes,
            paymentMethod: state.cart.paymentMethod});

        const response = await axios.post('http://192.168.1.2:8080/order/addToOrder?dishId=' + dishId, body, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const data = response.data;

        const newDish = data.order.find(item => item.dish.id === dishId);

        if(fromType === "fromMenu"){
            Toast.show({
                type: 'success',
                text1: 'Dodano do koszyka',
                text2: newDish.dish.name + ' x' + newDish.quantity
            })
        }

        dispatch({
            type: 'ADD_TO_CART',
            payload: {
                data: data
            },
        });
    } catch (error) {
        Toast.show({
            type: 'error',
            text1: 'Nie udało się dodać do zamówienia',
            text2: error
        });
        console.error('Error while adding to cart:', error);
    }
};

export const removeFromCart = (dishId) => async (dispatch, getState) => {
    try {
        const state = getState();

        let body = JSON.stringify({
            tableNoId: state.cart.tableNoId,
            cost: state.cart.cost,
            order: state.cart.dishes,
            paymentMethod: state.cart.paymentMethod});

        const response = await axios.post('http://192.168.1.2:8080/order/removeFromOrder?dishId=' + dishId, body, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const data = response.data;

        dispatch({
            type: 'REMOVE_FROM_CART',
            payload: {
                data: data
            },
        });
    } catch (error) {
        Toast.show({
            type: 'error',
            text1: 'Nie udało się usunąć z zamówienia',
            text2: error
        });
        console.error('Error while removing from cart:', error);
    }
};
