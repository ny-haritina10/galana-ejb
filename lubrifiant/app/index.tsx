import React, { useState, useRef } from 'react';
import {
  Text,
  View,
  TouchableOpacity,
  Animated,
  StyleSheet,
  Platform,
  StatusBar,
} from 'react-native';
import PrelevementForm from '@/components/PrelevementForm';
import EncaissementForm from '@/components/EncaissementForm';
import VenteProductList from '@/components/VenteProductList';

const SIDEBAR_WIDTH = 250;
const HEADER_HEIGHT = 60;
const STATUSBAR_HEIGHT = Platform.OS === 'android' ? StatusBar.currentHeight || 0 : 0;

export default function Index() {
  const [showPrelevementForm, setShowPrelevementForm] = useState(false);
  const [showEncaissementForm, setShowEncaissementForm] = useState(false);
  const [showVenteProductList, setShowVenteProductList] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  
  const sidebarAnimation = useRef(new Animated.Value(-SIDEBAR_WIDTH)).current;

  const toggleSidebar = () => {
    const toValue = isSidebarOpen ? -SIDEBAR_WIDTH : 0;
    
    Animated.timing(sidebarAnimation, {
      toValue,
      duration: 300,
      useNativeDriver: true,
    }).start();
    
    setIsSidebarOpen(!isSidebarOpen);
  };

  const MenuIcon = () => (
    <Text style={styles.menuIcon}>☰</Text>
  );

  const CloseIcon = () => (
    <Text style={styles.menuIcon}>✕</Text>
  );

  const SidebarButton = ({ title, onPress, isActive }: { 
    title: string; 
    onPress: () => void;
    isActive: boolean;
  }) => (
    <TouchableOpacity
      style={[styles.sidebarButton, isActive && styles.sidebarButtonActive]}
      onPress={onPress}
    >
      <Text style={[
        styles.sidebarButtonText,
        isActive && styles.sidebarButtonTextActive
      ]}>
        {title}
      </Text>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      {isSidebarOpen && (
        <TouchableOpacity
          style={styles.overlay}
          activeOpacity={1}
          onPress={toggleSidebar}
        />
      )}

      <View style={styles.headerContainer}>
        <View style={styles.header}>
          <TouchableOpacity onPress={toggleSidebar} style={styles.menuButton}>
            {isSidebarOpen ? <CloseIcon /> : <MenuIcon />}
          </TouchableOpacity>
          <View style={styles.title}>
            <Text style={styles.title}>Lubrifiant GALANA</Text>
          </View>
        </View>
      </View>

      <View style={styles.contentContainer}>
        <Animated.View 
          style={[
            styles.sidebar,
            {
              transform: [{ translateX: sidebarAnimation }],
            },
          ]}
        >
          <View style={styles.sidebarContent}>
            <SidebarButton 
              title="Prelevement Formulaire" 
              onPress={() => {
                setShowPrelevementForm(!showPrelevementForm);
                setShowEncaissementForm(false);
                setShowVenteProductList(false);
                toggleSidebar();
              }}
              isActive={showPrelevementForm}
            />
            <SidebarButton 
              title="Encaissement Formulaire" 
              onPress={() => {
                setShowEncaissementForm(!showEncaissementForm);
                setShowPrelevementForm(false);
                setShowVenteProductList(false);
                toggleSidebar();
              }}
              isActive={showEncaissementForm}
            />
            <SidebarButton 
              title="Vente Product List" 
              onPress={() => {
                setShowVenteProductList(!showVenteProductList);
                setShowPrelevementForm(false);
                setShowEncaissementForm(false);
                toggleSidebar();
              }}
              isActive={showVenteProductList}
            />
          </View>
        </Animated.View>

        <View style={styles.mainContent}>
          {showPrelevementForm && <PrelevementForm />}
          {showEncaissementForm && <EncaissementForm />}
          {showVenteProductList && <VenteProductList />}
          {!showPrelevementForm && !showEncaissementForm && !showVenteProductList && (
            <Text style={styles.placeholder}>
              Sélectionnez une option dans le menu
            </Text>
          )}
        </View>
      </View>
    </View>
  );
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  headerContainer: {
    paddingTop: STATUSBAR_HEIGHT,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#eee',
    zIndex: 3,
  },
  contentContainer: {
    flex: 1,
    position: 'relative',
  },
  overlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    zIndex: 1,
  },
  header: {
    height: HEADER_HEIGHT,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 15,
    backgroundColor: '#fff',
  },
  menuButton: {
    padding: 10,
    zIndex: 4,
  },
  menuIcon: {
    fontSize: 24,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginLeft: 20,
    flex: 1,
    textAlign: 'center',
  },
  sidebar: {
    position: 'absolute',
    left: 0,
    top: 0,
    bottom: 0,
    width: SIDEBAR_WIDTH,
    backgroundColor: '#fff',
    zIndex: 2,
    ...Platform.select({
      ios: {
        shadowColor: '#000',
        shadowOffset: { width: 2, height: 0 },
        shadowOpacity: 0.25,
        shadowRadius: 3.84,
      },
      android: {
        elevation: 5,
      },
    }),
  },
  sidebarContent: {
    flex: 1,
    paddingTop: 20,
   },
   sidebarButton:{
     paddingVertical :15 ,
     paddingHorizontal :20 ,
     borderBottomWidth :1 ,
     borderBottomColor :'#eee' ,
   },
   sidebarButtonActive:{
     backgroundColor :'#f0f0f0' ,
   },
   sidebarButtonText:{
     fontSize :16 ,
     color :'#333' ,
   },
   sidebarButtonTextActive:{
     color :'#000' ,
     fontWeight :'bold' ,
   },
   mainContent:{
     flex :1 ,
     justifyContent :'center' ,
     alignItems :'center' ,
     paddingHorizontal :20 ,
   },
   placeholder:{
     fontSize :16 ,
     color :'#666' ,
   },
});
