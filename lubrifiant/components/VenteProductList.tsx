import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList } from 'react-native';
import { API_ENDPOINTS } from '@/config/conf';

interface Product {
  id: number;
  unit: {
    id: number;
    name: string;
  };
  name: string;
  puAchat: number;
  puVente: number;
  typeProduct: string;
  qteInitial: number;
  sousType: string;
}

interface Pompe {
  id: number;
  product: Product;
  name: string;
  qteMax: number;
  qteInitial: number;
}

interface Vente {
  product: Product;
  pompe: Pompe;
  dateVente: string;
  sommeVentes: number;
}

const VenteProductList = () => {
  const [ventes, setVentes] = useState<Vente[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(API_ENDPOINTS.vente_products); 
        const data = await response.json();
        setVentes(data);
      } catch (error) {
        console.error('Error fetching ventes:', error);
      }
    };

    fetchData();
  }, []);

  const renderItem = ({ item }: { item: Vente }) => (
    <View style={styles.itemContainer}>
      <Text style={styles.itemText}>Product Name: {item.product.name}</Text>
      <Text style={styles.itemText}>Pompe Name: {item.pompe.name}</Text>
      <Text style={styles.itemText}>Date of Sale: {item.dateVente}</Text>
      <Text style={styles.itemText}>Total Sales Amount: {item.sommeVentes} €</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Sales Products List</Text>
      <FlatList
        data={ventes}
        renderItem={renderItem}
        keyExtractor={(item) => item.product.id.toString()}
        contentContainerStyle={styles.list}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    backgroundColor: '#F7FAFC',
    borderRadius: 12,
    shadowColor: '#171717',
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.1,
    shadowRadius: 10,
    elevation: 10,
    marginVertical: 10,
    width: '90%',
    alignSelf: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: '700',
    textAlign: 'center',
    marginBottom: 20,
    color: '#2A4365',
  },
  list: {
    paddingBottom: 20,
  },
  itemContainer: {
    backgroundColor: '#FFFFFF',
    borderRadius: 8,
    padding: 15,
    marginVertical: 10,
    shadowColor: '#171717',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.08,
    shadowRadius: 5,
    elevation: 3,
  },
  itemText: {
    fontSize: 16,
    color: '#2D3748',
    marginBottom: 5,
    fontWeight: '500',
  },
});

export default VenteProductList;