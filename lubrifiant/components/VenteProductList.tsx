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
    padding: 20,
    backgroundColor: '#f8f8f8',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 20,
  },
  list: {
    paddingBottom: 20,
  },
  itemContainer: {
    backgroundColor: '#ffffff',
    borderRadius: 8,
    padding: 15,
    marginVertical: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  itemText: {
    fontSize: 16,
    color: '#333',
    marginBottom: 5,
  },
});

export default VenteProductList;