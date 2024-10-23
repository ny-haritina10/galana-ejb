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

  // New state for selected values and inputs
  const [selectedProduct, setSelectedProduct] = useState<number | undefined>(undefined);
  const [selectedPompe, setSelectedPompe] = useState<number | undefined>(undefined);
  const [selectedPompiste, setSelectedPompiste] = useState<number | undefined>(undefined);
  const [quantity, setQuantity] = useState<string>('');
  const [datePrelevement, setDatePrelevement] = useState<string>(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    // Fetch data from the backend API
    const fetchData = async () => {
      const response = await fetch(API_ENDPOINTS.prelevement_lubrifiants);
      const data = await response.json();
      setProducts(data.products);
      setPompes(data.pompes);
      setPompistes(data.pompistes);
    };
    fetchData();
  }, []);

  // Handle form submission
  const handleSubmit = async () => {
    // Validate required fields
    if (!selectedProduct || !selectedPompe || !selectedPompiste || !quantity || !datePrelevement) {
      Alert.alert('Error', 'Please fill in all fields');
      return;
    }
  
    // Construct the data object
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
  
      const responseText = await response.text();
      console.log('Raw response:', responseText);
  
      // Add additional validation to ensure we have valid JSON
      if (!responseText.trim()) {
        throw new Error('Empty response received from server');
      }
  
      let responseData;
      try {
        // Remove any BOM and control characters before parsing
        const cleanText = responseText
          .replace(/^\uFEFF/, '') // Remove BOM if present
          .replace(/[\u0000-\u001F]+/g, ' ') // Replace control characters with space
          .trim();
        responseData = JSON.parse(cleanText);
      } catch (parseError) {
        console.error('JSON Parse error:', parseError);
        Alert.alert('Error', 'Invalid response from server');
        return;
      }
  
      if (responseData.status === 'success') {
        Alert.alert(
          'Success',
          responseData.message,
          [{ 
            text: 'OK',
            onPress: () => {
              // Reset form after successful submission
              setSelectedProduct(undefined);
              setSelectedPompe(undefined);
              setSelectedPompiste(undefined);
              setQuantity('');
              setDatePrelevement(new Date().toISOString().split('T')[0]);
            }
          }]
        );
      } else {
        Alert.alert(
          'Warning',
          responseData.message,
          [{ text: 'OK', onPress: () => console.log('Warning alert closed') }]
        );
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
      />

      <Text style={styles.label}>Date of Prelevement:</Text>
      <TextInput
        style={styles.input}
        value={datePrelevement}
        onChangeText={setDatePrelevement}
        placeholder="YYYY-MM-DD"
      />

      <Button title="Submit" onPress={handleSubmit} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
    width: '100%',
    alignItems: 'flex-start',
  },
  label: {
    fontSize: 18,
    marginBottom: 10,
    color: '#333',
  },
  picker: {
    height: 50,
    width: '100%',
    marginBottom: 20,
    backgroundColor: '#f0f0f0',
    borderRadius: 5,
    borderWidth: 1,
    borderColor: '#ccc',
  },
  input: {
    height: 50,
    width: '100%',
    marginBottom: 20,
    paddingHorizontal: 10,
    backgroundColor: '#f0f0f0',
    borderRadius: 5,
    borderWidth: 1,
    borderColor: '#ccc',
  },
});

export default PrelevementForm;