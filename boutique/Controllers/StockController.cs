using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using boutique.Models;

namespace boutique.Controllers
{
    public class StockController : Controller
    {
        private readonly HttpClient _httpClient;

        public StockController()
        {
            _httpClient = new HttpClient();
            _httpClient.BaseAddress = new Uri("http://localhost:8080/");
        }

        public async Task<IActionResult> Index()
        {
            try
            {
                var response = await _httpClient.GetAsync("galana-ejb/api/stocks");
                if (response.IsSuccessStatusCode)
                {
                    var jsonString = await response.Content.ReadAsStringAsync();
                    var stockDetails = JsonConvert.DeserializeObject<List<StockDetail>>(jsonString);

                    return View(stockDetails);
                }
                
                ViewBag.Error = $"Could not fetch stock data. Status: {response.StatusCode}";
                return View(new List<StockDetail>());
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error: " + ex.Message;
                return View(new List<StockDetail>());
            }
        }

        public async Task<IActionResult> Details(int id)
        {
            try
            {
                var response = await _httpClient.GetAsync("galana-ejb/api/stocks");
                if (response.IsSuccessStatusCode)
                {
                    var jsonString = await response.Content.ReadAsStringAsync();
                    var stockDetails = JsonConvert.DeserializeObject<List<StockDetail>>(jsonString);

                    var stockDetail = stockDetails.Find(s => s.StockId == id);

                    if (stockDetail != null)
                    {
                        return View(stockDetail);
                    }
                }
                
                ViewBag.Error = $"Could not fetch stock detail. Status: {response.StatusCode}";
                return View(new StockDetail());
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error: " + ex.Message;
                return View(new StockDetail());
            }
        }
    }
}