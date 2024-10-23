import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList } from 'react-native';
import { API_ENDPOINTS } from '@/config/conf'; 

interface Product {
  productId: number;
  productName: string;
  initialQuantity: number;
  totalQuantityIn: number;
  totalQuantityOut: number;
  remainingQuantity: number;
}

const StockList = () => {
  const [products, setProducts] = useState<Product[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(API_ENDPOINTS.stock_remaining);
        const data = await response.json();
        setProducts(data);
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    fetchData();
  }, []);

  const renderItem = ({ item }: { item: Product }) => (
    <View style={styles.itemContainer}>
      <Text style={styles.itemText}>Product Name: {item.productName}</Text>
      <Text style={styles.itemText}>Initial Quantity: {item.initialQuantity}</Text>
      <Text style={styles.itemText}>Total Quantity In: {item.totalQuantityIn}</Text>
      <Text style={styles.itemText}>Total Quantity Out: {item.totalQuantityOut}</Text>
      <Text style={styles.itemText}>Remaining Quantity: {item.remainingQuantity}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Product List</Text>
      <FlatList
        data={products}
        renderItem={renderItem}
        keyExtractor={(item) => item.productId.toString()} // Ensure this ID is unique
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

export default StockList;