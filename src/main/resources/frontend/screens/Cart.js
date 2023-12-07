import React, {useEffect, useState} from 'react';
import {Image, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View} from 'react-native';
import Toast from 'react-native-toast-message';
import Dialog from "react-native-dialog";
import {useDispatch, useSelector} from 'react-redux';
import {acceptOrder, addToCart, removeFromCart, savePaymentMethod} from '../actions/actions';
import axios from 'axios';

const Cart = (props) => {

    const [visible, setVisible] = useState(false);
    const [tableNumber, setTableNumber] = useState('');

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

    const cart = useSelector((state) => state.cart.dishes);
    const tableNumberId = useSelector((state) => state.cart.tableNoId);
    const cost = useSelector((state) => state.cart.cost);
    const paymentMethod = useSelector((state) => state.cart.paymentMethod);

    const sendOrder = async () => {
        dispatch(acceptOrder());
        setVisible(false);
        Toast.show({
            type: 'success',
            text1: 'Złożono zamówienie',
        })
    };

    useEffect(() => {
        const fetchData = async () => {
            if (tableNumberId !== '' && tableNumberId !== null) {
                try {
                    const response = await axios.get('http://192.168.1.2:8080/qrCode/getValue/' + tableNumberId, {
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    });
                    setTableNumber(response.data.qrCode);
                } catch (error) {
                    Toast.show({
                        type: 'error',
                        text1: 'Nie udało się pobrać numeru stolika',
                    });
                }
            }
        };

        let a = fetchData();

        // The cleanup function
        return () => {};
    }, [tableNumberId]);

    const addItemToCart = (item) => {
        dispatch(addToCart(item.id, "fromCart"));
    };

    const removeItemFromCart = (item) => {
        dispatch(removeFromCart(item.id));
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

    const handlePaymentMethod = (paymentMethod) => {
        dispatch(savePaymentMethod(paymentMethod));
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
                                        <View key={index} style={styles.dishContainer}>
                                            <View style={styles.dishDescription}>
                                                <Text style={styles.dishName}>{dish.dish.name}</Text>
                                                <Text style={styles.dishPrice}> {dish.dish.price} x{dish.quantity}: {(dish.dish.price * dish.quantity).toFixed(2)} zł</Text>
                                            </View>
                                            <View style={styles.dishAction}>
                                                <TouchableOpacity onPress={() => {
                                                    removeItemFromCart(dish.dish)
                                                }} style={styles.controlButton}>
                                                    <Text style={styles.controlButtonText}>-</Text>
                                                </TouchableOpacity>
                                                <Text style={styles.quantity}>{dish.quantity}</Text>
                                                <TouchableOpacity onPress={() => {
                                                    addItemToCart(dish.dish)
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
