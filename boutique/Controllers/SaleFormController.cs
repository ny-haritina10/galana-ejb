using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using boutique.Models;

namespace boutique.Controllers
{
    public class SaleFormController : Controller
    {
        private readonly HttpClient _httpClient;

        public SaleFormController()
        {
            _httpClient = new HttpClient();
            _httpClient.BaseAddress = new Uri("http://localhost:8080/");
        }

        public async Task<IActionResult> Index()
        {
            try 
            {
                var apiData = await GetFormDataFromApi();
                var model = new SaleForm
                {
                    SaleDate = DateTime.Now,
                    Clients = apiData.Clients,
                    Products = apiData.Products
                };

                return View(model);
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error: " + ex.Message;
                return View(new SaleForm
                {
                    SaleDate = DateTime.Now,
                    Clients = new List<Client>(),
                    Products = new List<Product>()
                });
            }
        }

        private async Task<ApiResponse> GetFormDataFromApi()
        {
            var response = await _httpClient.GetAsync("galana-ejb/api/sale_form");
            if (response.IsSuccessStatusCode)
            {
                var jsonString = await response.Content.ReadAsStringAsync();
                return JsonConvert.DeserializeObject<ApiResponse>(jsonString) 
                    ?? throw new Exception("Failed to deserialize API response");
            }
            
            throw new Exception($"API request failed with status: {response.StatusCode}");
        }
    }

    public class ApiResponse
    {
        public List<Product> Products { get; set; } = new();
        public List<Client> Clients { get; set; } = new();
    }
}