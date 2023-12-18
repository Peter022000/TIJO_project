const initialState = {
    dishes: [],
    tableNoId: '6576b67b0d86b15e669bff3f',
    paymentMethod: null,
    cost: '',
};

const CartReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'REMOVE_FROM_CART':
        case 'ADD_TO_CART':
            return {
                dishes: action.payload.data.order,
                tableNoId: action.payload.data.tableNoId,
                paymentMethod: action.payload.data.paymentMethod,
                cost: action.payload.data.cost
            };
        case "ACCEPT_ORDER":
            return {
                ...initialState
            };
        case "SAVE_PAYMENT_METHOD":
            return {
                ...state,
                paymentMethod: action.payload.data,
            };
        default:
            return state;
    }
};

export default CartReducer;
