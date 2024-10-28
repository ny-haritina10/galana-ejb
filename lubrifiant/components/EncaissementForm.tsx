import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TextInput, Button, Alert } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { API_ENDPOINTS } from '@/config/conf';

interface Prelevement {
  id: number;
  pompiste: { id: number; name: string };
  pompe: { id: number; product: { id: number; name: string }; name: string };
  product: { id: number; name: string };
  amount: number;
  datePrelevement: string;
}

const EncaissementForm = () => {
  const [prelevements, setPrelevements] = useState<Prelevement[]>([]);
  const [selectedPrelevementId, setSelectedPrelevementId] = useState<number | undefined>(undefined);
  const [montantEncaisser, setMontantEncaisser] = useState<string>('');
  const [dateEncaissement, setDateEncaissement] = useState<string>(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(API_ENDPOINTS.encaissement_lubrifiants);
      const data = await response.json();
      setPrelevements(data.prelevements);
    };
    fetchData();
  }, []);

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
          .sort((a, b) => a.id - b.id)
          .map((prelevement) => (
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
        placeholderTextColor="#7d7d7d"
      />

      <Text style={styles.label}>Date d'encaissement:</Text>
      <TextInput
        style={styles.input}
        value={dateEncaissement}
        onChangeText={setDateEncaissement}
        placeholder="YYYY-MM-DD"
        placeholderTextColor="#7d7d7d"
      />

      <View style={styles.buttonContainer}>
        <Button title="Submit" onPress={handleSubmit} color="#5A67D8" />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
    width: '90%',
    alignSelf: 'center',
    backgroundColor: '#fff',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 6,
    elevation: 8,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 10,
    color: '#2D3748',
  },
  picker: {
    height: 50,
    width: '100%',
    marginBottom: 20,
    backgroundColor: '#EDF2F7',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#CBD5E0',
    paddingHorizontal: 10,
  },
  input: {
    height: 50,
    width: '100%',
    marginBottom: 20,
    paddingHorizontal: 15,
    backgroundColor: '#EDF2F7',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#CBD5E0',
    fontSize: 16,
    color: '#2D3748',
  },
  buttonContainer: {
    marginTop: 10,
    borderRadius: 8,
    overflow: 'hidden',
  },
});

export default EncaissementForm;