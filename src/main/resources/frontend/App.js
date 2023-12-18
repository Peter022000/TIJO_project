import React from 'react';
import {Text, View} from 'react-native';
import {NavigationContainer} from '@react-navigation/native';
import Tabs from './navigation/Tabs';
import {Provider} from 'react-redux';
import store from './store/store';
import Toast, {BaseToast, ErrorToast} from 'react-native-toast-message';

const toastConfig = {
    success: (props) => (
        <BaseToast
            {...props}
            style={{ borderLeftColor: 'green', height: 100 }}
            contentContainerStyle={{ paddingHorizontal: 20 }}
            text1Style={{
                fontSize: 20,
                fontWeight: '400',
            }}
            text1NumberOfLines={5}
            text2Style={{
                fontSize: 15,
                fontWeight: '300',
            }}
            text2NumberOfLines={5}
        />
    ),
    error: (props) => (
        <ErrorToast
            {...props}
            text1Style={{
                fontSize: 20,
                fontWeight: '400'
            }}
            text2Style={{
                fontSize: 15,
                fontWeight: '300'
            }}
        />
    )
};

const App = () => {

    return (
        <>
            <Provider store={store}>
                <NavigationContainer>
                    <Tabs/>
                </NavigationContainer>
            </Provider>
            <Toast visibilityTime={2000} config={toastConfig}/>
        </>
    );
};

export default App;
