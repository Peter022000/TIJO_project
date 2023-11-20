import React from 'react';
import {Button, Text, View} from 'react-native';

const AboutUs = (props) => {

    return (
        <View>
            <Button onPress={() => props.navigation.navigate('AboutUs2')}>About us</Button>
        </View>
    );
};

export default AboutUs;
