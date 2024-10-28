import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TextInput, Button, Alert } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { API_ENDPOINTS } from '@/config/conf';

// Interface for JSON data
interface Product {
  id: number;
  name: string;
}

interface Pompe {
  id: number;
  name: string;
}

interface Pompiste {
  id: number;
  name: string;
}

const PrelevementForm = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [pompes, setPompes] = useState<Pompe[]>([]);
  const [pompistes, setPompistes] = useState<Pompiste[]>([]);

  const [selectedProduct, setSelectedProduct] = useState<number | undefined>(undefined);
  const [selectedPompe, setSelectedPompe] = useState<number | undefined>(undefined);
  const [selectedPompiste, setSelectedPompiste] = useState<number | undefined>(undefined);
  const [quantity, setQuantity] = useState<string>('');
  const [datePrelevement, setDatePrelevement] = useState<string>(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(API_ENDPOINTS.prelevement_lubrifiants);
      const data = await response.json();
      setProducts(data.products);
      setPompes(data.pompes);
      setPompistes(data.pompistes);
    };
    fetchData();
  }, []);

  const handleSubmit = async () => {
    if (!selectedProduct || !selectedPompe || !selectedPompiste || !quantity || !datePrelevement) {
      Alert.alert('Error', 'Please fill in all fields');
      return;
    }
  
    const data = {
      productId: selectedProduct,
      pompeId: selectedPompe,
      pompisteId: selectedPompiste,
      quantity: quantity,
      datePrelevement: datePrelevement,
    };
  
    try {
      const response = await fetch(API_ENDPOINTS.prelevement_lubrifiants, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
  
      const responseData = await response.json();
      if (responseData.status === 'success') {
        Alert.alert(
          'Success',
          responseData.message,
          [{ 
            text: 'OK',
            onPress: () => {
              setSelectedProduct(undefined);
              setSelectedPompe(undefined);
              setSelectedPompiste(undefined);
              setQuantity('');
              setDatePrelevement(new Date().toISOString().split('T')[0]);
            }
          }]
        );
      } else {
        Alert.alert('Warning', responseData.message);
      }
    } catch (error) {
      console.error('Network error:', error);
      Alert.alert('Error', 'An error occurred while sending data');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>Select Product:</Text>
      <Picker 
        style={styles.picker} 
        selectedValue={selectedProduct}
        onValueChange={(itemValue) => setSelectedProduct(itemValue)}
      >
        <Picker.Item label="Select a product" value={undefined} />
        {products.map(product => (
          <Picker.Item key={product.id} label={product.name} value={product.id} />
        ))}
      </Picker>

      <Text style={styles.label}>Select Pompe:</Text>
      <Picker 
        style={styles.picker} 
        selectedValue={selectedPompe}
        onValueChange={(itemValue) => setSelectedPompe(itemValue)}
      >
        <Picker.Item label="Select a pompe" value={undefined} />
        {pompes.map(pompe => (
          <Picker.Item key={pompe.id} label={pompe.name} value={pompe.id} />
        ))}
      </Picker>

      <Text style={styles.label}>Select Pompiste:</Text>
      <Picker 
        style={styles.picker} 
        selectedValue={selectedPompiste}
        onValueChange={(itemValue) => setSelectedPompiste(itemValue)}
      >
        <Picker.Item label="Select a pompiste" value={undefined} />
        {pompistes.map(pompiste => (
          <Picker.Item key={pompiste.id} label={pompiste.name} value={pompiste.id} />
        ))}
      </Picker>

      <Text style={styles.label}>Enter Quantity:</Text>
      <TextInput
        style={styles.input}
        value={quantity}
        onChangeText={setQuantity}
        keyboardType="numeric"
        placeholder="Enter quantity"
        placeholderTextColor="#A0AEC0"
      />

      <Text style={styles.label}>Date of Prelevement:</Text>
      <TextInput
        style={styles.input}
        value={datePrelevement}
        onChangeText={setDatePrelevement}
        placeholder="YYYY-MM-DD"
        placeholderTextColor="#A0AEC0"
      />

      <View style={styles.buttonContainer}>
        <Button title="Submit" onPress={handleSubmit} color="#4C51BF" />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 24,
    marginVertical: 10,
    width: '90%',
    alignSelf: 'center',
    backgroundColor: '#F7FAFC',
    borderRadius: 12,
    shadowColor: '#171717',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.1,
    shadowRadius: 10,
    elevation: 10,
  },
  label: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 8,
    color: '#2A4365',
  },
  picker: {
    height: 55,
    width: '100%',
    marginBottom: 18,
    backgroundColor: '#E2E8F0',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#CBD5E0',
    paddingHorizontal: 10,
  },
  input: {
    height: 55,
    width: '100%',
    marginBottom: 18,
    paddingHorizontal: 15,
    backgroundColor: '#E2E8F0',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#CBD5E0',
    fontSize: 17,
    color: '#2D3748',
  },
  buttonContainer: {
    marginTop: 12,
    borderRadius: 8,
    overflow: 'hidden',
  },
});

export default PrelevementForm;