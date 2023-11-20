import React, {useEffect, useState} from 'react';
import {Image, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View} from 'react-native';
import {clearCart, decrementQty, incrementQty, setTableNumber} from '../reducer/CartReducer';
import { useDispatch, useSelector } from "react-redux"
import Toast from 'react-native-toast-message';
import Dialog from "react-native-dialog";

const Cart = (props) => {

    const [paymentMethod, setPaymentMethod] = useState('');
    const [visible, setVisible] = useState(false);

    const dispatch = useDispatch();

    const showDialog = () => {
        setVisible(true);
    };

    const handleCancel = () => {
        setVisible(false);
    };

    const handleSend = () => {
        let a = sendOrder();
    };

    const cart = useSelector((state) => state.cart.cart);
    const tableNumber = useSelector((state) => state.cart.tableNumber);
    const cost = (cart.reduce((sum, cost) => {return sum + (cost.price * cost.quantity)}, 0)).toFixed(2);

    const sendOrder = async () => {
        try {
            const response = await fetch('http://192.168.1.2:8080/order/sendOrder', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    tableNo: Number(tableNumber),
                    cost: Number(cost),
                    order: cart.map(({id, name, price, quantity}) => ({id, name, price, quantity})),
                    paymentMethod: paymentMethod
                })
            });

            setVisible(false);
            Toast.show({
                type: 'success',
                text1: 'Złożono zamówienie',
            });
            dispatch(clearCart());
            setTableNumber('');
            setPaymentMethod('');
        } catch (error) {
            console.error(error);
            Toast.show({
                type: 'error',
                text1: 'Nie udało się złożyć zamówienia',
                text2: error
            });
        }
    };

    const validate = () => {
        if(cart.length === 0) {
            Toast.show({
                type: 'error',
                text1: 'Niepoprawne zamówienie',
                text2: 'Koszyk jest pusty',
            });
        }else if(paymentMethod === '') {
            Toast.show({
                type: 'error',
                text1: 'Niepoprawne zamówienie',
                text2: 'Brak metody płatności',
            });
        }else if(tableNumber === ''){
            Toast.show({
                type: 'error',
                text1: 'Niepoprawne zamówienie',
                text2: 'Brak numeru stolika',
            });
        }else{
            showDialog();
        }
    }

    const handlePaymentMethod = (method) => {
        setPaymentMethod(method);
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <View>
                {cart.length !== 0 ?
                    <>
                        <Text style={styles.title}>Twój koszyk:</Text>
                        <View>
                            {
                                cart.map((dish, index) => {
                                    return (
                                        <View key={index + '_soup'} style={styles.dishContainer}>
                                            <View style={styles.dishDescription}>
                                                <Text style={styles.dishName}>{dish.name}</Text>
                                                <Text style={styles.dishPrice}> {dish.price} x{dish.quantity}: {(dish.price * dish.quantity).toFixed(2)} zł</Text>
                                            </View>
                                            <View style={styles.dishAction}>
                                                <TouchableOpacity onPress={() => {
                                                    dispatch(decrementQty(dish))
                                                }} style={styles.controlButton}>
                                                    <Text style={styles.controlButtonText}>-</Text>
                                                </TouchableOpacity>
                                                <Text style={styles.quantity}>{dish.quantity}</Text>
                                                <TouchableOpacity onPress={() => {
                                                    dispatch(incrementQty(dish))
                                                }} style={styles.controlButton}>
                                                    <Text style={styles.controlButtonText}>+</Text>
                                                </TouchableOpacity>
                                            </View>
                                        </View>
                                    )
                                })
                            }
                        </View>
                        <View>
                            <Text style={styles.title}>Razem {cost} </Text>
                        </View>
                    </>
                    : <Text style={styles.title}>Koszyk jest pusty</Text>
                }
                <View style={styles.payment}>
                    <Text style={styles.paymentTitle}>Metoda płatności:</Text>
                    <TouchableOpacity onPress={() => handlePaymentMethod('cash')}>
                        <Text
                            style={paymentMethod === 'cash' ? styles.selectedPayment : styles.paymentMethod}>Gotówka</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => handlePaymentMethod('card')}>
                        <Text
                            style={paymentMethod === 'card' ? styles.selectedPayment : styles.paymentMethod}>Karta</Text>
                    </TouchableOpacity>
                </View>
                <View style={styles.table}>
                    <Text style={styles.tableTitle}>Numer stolika: {tableNumber}</Text>
                </View>
                <TouchableOpacity onPress={() => validate()} style={styles.checkoutButton}>
                    <Text style={styles.checkoutButtonText}>Zamów</Text>
                </TouchableOpacity>
            </View>
            <Dialog.Container visible={visible}>
                <Dialog.Title>Zamówienie</Dialog.Title>
                <Dialog.Description>
                    Czy na pewno chcesz złożyć zamówienie
                </Dialog.Description>
                <Dialog.Button label="Anuluj" onPress={handleCancel} />
                <Dialog.Button label="Złóż" onPress={handleSend} />
            </Dialog.Container>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        flexGrow: 1,
        padding: 16,
        backgroundColor: '#fff',
        paddingBottom: 140
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 16,
    },
    itemName: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    quantity: {
        fontSize: 18,
        marginHorizontal: 8,
    },
    itemPrice: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    payment: {
        marginVertical: 16,
    },
    paymentTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 8,
    },
    paymentMethod: {
        fontSize: 16,
        marginBottom: 8,
    },
    selectedPayment: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 8,
        color: '#0066ff',
    },
    table: {
        marginVertical: 16,
    },
    tableTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 8,
    },
    tableInput: {
        fontSize: 16,
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 4,
        padding: 8,
    },
    checkoutButton: {
        backgroundColor: '#ff7043',
        padding: 16,
        borderRadius: 8,
        marginTop: 16,
    },
    checkoutButtonText: {
        color: '#fff',
        fontSize: 20,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    controlButton: {
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#fff',
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 4,
        paddingVertical: 8,
        paddingHorizontal: 16,
        marginHorizontal: 4,
    },
    controlButtonText: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333',
    },
    dishContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: 15,
        paddingVertical: 10,
    },
    dishDescription: {
        flex: 1,
    },
    dishName: {
        fontSize: 22,
        fontWeight: 'bold',
        color: '#333333',
        marginBottom: 5,
    },
    dishPrice: {
        fontSize: 18,
        color: '#666666',
        marginBottom: 5,
    },
    dishAction: {
        flex: 0.5,
        flexDirection: 'row',
        borderRadius: 15,
        alignItems: 'center',
        justifyContent: 'center',
    },
});

export default Cart;
