using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using boutique.Models;

namespace boutique.Controllers
{
    public class StockRemainingController : Controller
    {
        private readonly HttpClient _httpClient;

        public StockRemainingController()
        {
            _httpClient = new HttpClient();
            _httpClient.BaseAddress = new Uri("http://localhost:8080/");
        }

        public async Task<IActionResult> Index()
        {
            try
            {
                var response = await _httpClient.GetAsync("galana-ejb/api/stock_remaining");
                if (response.IsSuccessStatusCode)
                {
                    var jsonString = await response.Content.ReadAsStringAsync();
                    var stockRemainings = JsonConvert.DeserializeObject<List<StockRemaining>>(jsonString);

                    return View(stockRemainings);
                }
                
                ViewBag.Error = $"Could not fetch stock data. Status: {response.StatusCode}";
                return View(new List<StockRemaining>());
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error: " + ex.Message;
                return View(new List<StockRemaining>());
            }
        }      
    }
}