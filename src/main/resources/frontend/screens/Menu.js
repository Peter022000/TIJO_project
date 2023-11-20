import React, {useEffect, useState} from 'react';
import {
    ScrollView,
    StyleSheet,
    TouchableOpacity,
    Text,
    View,
    Image,
    ActivityIndicator,
    RefreshControl,
} from 'react-native';
import { useDispatch } from "react-redux";
import { addToCart } from "../reducer/CartReducer";

const Menu = (props) => {

    const [selection, setSelection] = useState(-1);
    const [soups, setSoups] = useState({});
    const [mainCourse, setMainCourse] = useState({});
    const [isLoading, setLoading] = useState(true);
    const [refreshing, setRefreshing] = useState(false);

    const dispatch = useDispatch();

    const addItemToCart = (item) => {
        dispatch(addToCart(item));
    };

    const getDishes = async () => {
        try {
            let string = 'http://192.168.1.2:8080/dishes/getAllDishes';
            const response = await fetch(string);
            const json = await response.json();

            setSoups(json.filter(t => t.dishType === "soup"));
            setMainCourse(json.filter(t => t.dishType === "mainCourse"));
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    }

    const onRefresh = React.useCallback(() => {
        setRefreshing(true);
        let a = getDishes()
        setRefreshing(false);
    }, []);


    useEffect(() => {
        return props.navigation.addListener("focus", () => {
            let a = getDishes();
        });
    }, [props.navigation]);

    return (
        <ScrollView
            contentContainerStyle={styles.container}
            refreshControl={
                <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
            }>
            <View>
                <View style={styles.menuHeaderContainer}>
                    <Text style={styles.menuHeader}>Menu</Text>
                </View>
                {isLoading ? <ActivityIndicator/> : (
                    <>
                        <View style={styles.categoryContainer}>
                            <TouchableOpacity onPress={() => {(selection === 0) ? setSelection(-1) : setSelection(0)}}>
                                <Text style={styles.menuHeader}>Zupy</Text>
                            </TouchableOpacity>

                            {
                                (selection === 0) ?
                                    soups.map((dish, index) => {
                                        return(
                                            <View key={index + '_soups'} style={styles.dishContainer}>
                                                <View style={styles.dishDescription}>
                                                    <Text style={styles.dishName}>{dish.name}</Text>
                                                    <Text style={styles.dishPrice}>{dish.price} zł</Text>
                                                    <Text style={styles.dishIngredients}>{dish.ingredients.join(', ')}</Text>
                                                </View>
                                                <TouchableOpacity onPress={() =>{addItemToCart(dish)}} style={styles.dishAction}>
                                                    <Image
                                                        source={require("../assets/icons/add.png")}
                                                        resizeMode={'contain'}
                                                        style={{
                                                            width: 30,
                                                            height: 30,
                                                        }}
                                                    />
                                                </TouchableOpacity>
                                            </View>
                                        )
                                    }) : <></>
                            }
                        </View>
                        <View style={styles.categoryContainer}>
                            <TouchableOpacity onPress={() => (selection === 1) ? setSelection(-1) : setSelection(1)}>
                                <Text style={styles.menuHeader}>Dania główne</Text>
                            </TouchableOpacity>

                            {(selection === 1) ?
                                mainCourse.map((dish, index) => {
                                    return(
                                        <View key={index + '_main'} style={styles.dishContainer}>
                                            <View style={styles.dishDescription}>
                                                <Text style={styles.dishName}>{dish.name}</Text>
                                                <Text style={styles.dishPrice}>{dish.price} zł</Text>
                                                <Text style={styles.dishIngredients}>{dish.ingredients.join(', ')}</Text>
                                            </View>
                                            <TouchableOpacity onPress={() =>{addItemToCart(dish)}} style={styles.dishAction}>
                                                <Image
                                                    source={require("../assets/icons/add.png")}
                                                    resizeMode={'contain'}
                                                    style={{
                                                        width: 30,
                                                        height: 30,
                                                    }}
                                                />
                                            </TouchableOpacity>
                                        </View>
                                    )
                                }) : <></>
                            }
                        </View>
                    </>
                )}

            </View>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        flexGrow: 1,
        backgroundColor: '#f5f5f5',
        paddingBottom: 110
    },
    menuHeaderContainer: {
        marginTop: 50,
        marginBottom: 20,
        alignItems: 'center',
    },
    menuHeader: {
        fontSize: 36,
        fontWeight: 'bold',
        color: '#333333',
    },
    categoryContainer: {
        backgroundColor: '#ffffff',
        borderRadius: 20,
        marginHorizontal: 20,
        marginBottom: 30,
        paddingVertical: 20,
        paddingHorizontal: 15,
        shadowColor: '#000000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 5,
        elevation: 5,
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
        marginRight: 20,
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
    dishIngredients: {
        fontSize: 16,
        color: '#999999',
        marginBottom: 10,
    },
    dishAction: {
        backgroundColor: '#FFD600',
        borderRadius: 15,
        width: 45,
        height: 45,
        alignItems: 'center',
        justifyContent: 'center',
    },
    dishActionIcon: {
        tintColor: '#ffffff',
    }
});

export default Menu;
