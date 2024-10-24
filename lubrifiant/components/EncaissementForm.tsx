import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TextInput, Button, Alert } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { API_ENDPOINTS } from '@/config/conf';

// Interface for Prelevement data
interface Prelevement {
  id: number;
  pompiste: {
    id: number;
    name: string;
  };
  pompe: {
    id: number;
    product: {
      id: number;
      name: string;
    };
    name: string;
  };
  product: {
    id: number;
    name: string;
  };
  amount: number;
  datePrelevement: string;
}

const EncaissementForm = () => {
  const [prelevements, setPrelevements] = useState<Prelevement[]>([]);
  const [selectedPrelevementId, setSelectedPrelevementId] = useState<number | undefined>(undefined);
  const [montantEncaisser, setMontantEncaisser] = useState<string>('');
  const [dateEncaissement, setDateEncaissement] = useState<string>(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    // Fetch prelevements data from the backend API
    const fetchData = async () => {
      const response = await fetch(API_ENDPOINTS.encaissement_lubrifiants); 
      const data = await response.json();
      setPrelevements(data.prelevements); 
    };

    fetchData();
  }, []);

  // Handle form submission
  const handleSubmit = async () => {
    const data = {
      prelevementId: selectedPrelevementId,
      montantEncaisser: montantEncaisser,
      dateEncaissement: dateEncaissement,
    };

    try {
      const response = await fetch(API_ENDPOINTS.encaissement_lubrifiants, { 
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
      });

      if (response.ok) {
        Alert.alert('Success', 'Data sent successfully!');
      } else {
        Alert.alert('Error', 'Failed to send data.');
      }
    } catch (error) {
      console.error('Error:', error);
      Alert.alert('Error', 'An error occurred while sending data.');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>Select Prelevement:</Text>
      <Picker 
        style={styles.picker} 
        selectedValue={selectedPrelevementId}
        onValueChange={(itemValue) => setSelectedPrelevementId(itemValue)}
      >
        {prelevements
          .sort((a, b) => a.id - b.id)  // Sort by id in ascending order 
          .map(prelevement => (
            <Picker.Item 
              key={prelevement.id} 
              label={`ID: ${prelevement.id} - ${prelevement.product.name} - Amount: ${prelevement.amount}`} 
              value={prelevement.id} 
            />
          ))}
      </Picker>
      
      <Text style={styles.label}>Montant à encaisser:</Text>
      <TextInput
        style={styles.input}
        value={montantEncaisser}
        onChangeText={setMontantEncaisser}
        keyboardType="numeric"
        placeholder="Enter amount"
      />

      <Text style={styles.label}>Date d'encaissement:</Text>
      <TextInput
        style={styles.input}
        value={dateEncaissement}
        onChangeText={setDateEncaissement}
        placeholder="YYYY-MM-DD"
      />

      {/* Submit Button */}
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

export default EncaissementForm;