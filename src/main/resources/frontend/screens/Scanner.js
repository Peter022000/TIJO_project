import React, { useState, useEffect } from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useCameraDevices } from 'react-native-vision-camera';
import { Camera } from 'react-native-vision-camera';
import { useScanBarcodes, BarcodeFormat } from 'vision-camera-code-scanner';
import { useDispatch } from 'react-redux';
import Toast from 'react-native-toast-message';

const Scanner = (props) => {
    const [hasPermission, setHasPermission] = useState(false);
    const [scanActive, setScanActive] = useState(true);
    const [error, setError] = useState(false);
    const [qrCode, setQrCode] = useState('');
    const devices = useCameraDevices();
    const device = devices.back;
    const dispatch = useDispatch();

    const [frameProcessor, barcodes] = useScanBarcodes([BarcodeFormat.QR_CODE], {
        checkInverted: true,
    });

    useEffect(() => {
        if (barcodes.length > 0) {
            getValue(barcodes.at(0).displayValue);
        }
    }, [barcodes]);

    useEffect(() => {
        if (qrCode !== '') {
            setScanActive(false);
        }
    }, [qrCode]);

    useEffect(() => {
        return props.navigation.addListener('focus', () => {
            setHasPermission(false);
            setScanActive(true);
            setQrCode('');
            (async () => {
                const status = await Camera.requestCameraPermission();
                setHasPermission(status === 'authorized');
            })();
        });
    }, [props.navigation]);

    //todo zabezpieczyć przed złym kodem qr
    const getValue = async (id) => {
        try {
            const response = await fetch('http://192.168.1.2:8080/qrCode/getValue/'+id, {
                method: 'GET',
            });

            if(response.ok) {
                const json = await response.json();
                setQrCode(json.qrCode);
            } else {
                setScanActive(false);
                setError(true);
            }

        } catch (error) {
            console.error(error);
        }
    };


    const handleConfirmCode = () => {
        dispatch(setTableNumber(qrCode));
        Toast.show({
            type: 'success',
            text1: 'Kod QR zrealizowany',
        });
        // Dodaj tutaj inne akcje, które mają być wykonywane po zatwierdzeniu kodu
    };

    const handleRepeatScan = () => {
        setError(false);
        setScanActive(true);
        setQrCode('');
    };

    const displayScanner = () => {
        return (
            device != null &&
            hasPermission && (
                <>
                    <Camera
                        style={StyleSheet.absoluteFill}
                        device={device}
                        isActive={true}
                        frameProcessor={frameProcessor}
                        frameProcessorFps={5}
                    />
                    <TouchableOpacity
                        style={styles.scanButton}
                        onPress={() => setScanActive(false)}
                    >
                        <Text style={styles.scanButtonText}>Skanuj</Text>
                    </TouchableOpacity>
                </>
            )
        );
    };

    const displayError = () => {
        return (
            <View style={styles.scannedCodeContainer}>
                <Text style={styles.scannedCode}>Błędny kod QR</Text>
                <TouchableOpacity
                    style={styles.repeatButton}
                    onPress={handleRepeatScan}
                >
                    <Text style={styles.buttonText}>Powtórz skanowanie</Text>
                </TouchableOpacity>
            </View>
        );
    };


    const displayScannedCode = () => {
        return (
            <View style={styles.scannedCodeContainer}>
                <Text style={styles.scannedCode}>Numer stolika</Text>
                <Text style={styles.scannedCode}>{qrCode}</Text>
                <TouchableOpacity
                    style={styles.confirmButton}
                    onPress={handleConfirmCode}
                >
                    <Text style={styles.buttonText}>Zatwierdź kod</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={styles.repeatButton}
                    onPress={handleRepeatScan}
                >
                    <Text style={styles.buttonText}>Powtórz skanowanie</Text>
                </TouchableOpacity>
            </View>
        );
    };

    return <>{scanActive ? displayScanner() : (error ? displayError() : displayScannedCode())}</>;
};

const styles = StyleSheet.create({
    scanButton: {
        position: 'absolute',
        bottom: 20,
        alignSelf: 'center',
        backgroundColor: 'blue',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 10,
    },
    scanButtonText: {
        color: 'white',
        fontSize: 18,
        fontWeight: 'bold',
    },
    scannedCodeContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    scannedCode: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
    },
    confirmButton: {
        backgroundColor: 'green',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 10,
        marginBottom: 10,
    },
    repeatButton: {
        backgroundColor: 'red',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 10,
    },
    buttonText: {
        color: 'white',
        fontSize: 18,
        fontWeight: 'bold',
    },
});

export default Scanner;
