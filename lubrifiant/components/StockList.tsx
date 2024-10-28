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
        keyExtractor={(item) => item.productId.toString()}
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

export default StockList;