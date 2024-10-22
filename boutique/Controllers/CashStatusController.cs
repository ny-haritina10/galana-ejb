using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using boutique.Models;

namespace boutique.Controllers
{
    public class CashStatusController : Controller
    {
        private readonly HttpClient _httpClient;

        public CashStatusController()
        {
            _httpClient = new HttpClient();
            _httpClient.BaseAddress = new Uri("http://localhost:8080/");
        }

        public async Task<IActionResult> Index()
        {
            try
            {
                var response = await _httpClient.GetAsync("galana-ejb/api/cash");
                if (response.IsSuccessStatusCode)
                {
                    var jsonString = await response.Content.ReadAsStringAsync();
                    var saleInvoices = JsonConvert.DeserializeObject<List<SaleInvoice>>(jsonString);

                    // Calculate total cash
                    double totalCash = 0;
                    foreach (var invoice in saleInvoices)
                    {
                        if (invoice.InvoiceType == "PAYE")
                        { totalCash += invoice.TotalAmount; }
                    }

                    ViewBag.TotalCash = totalCash;
                    return View(saleInvoices);
                }
                
                ViewBag.Error = $"Could not fetch cash status data. Status: {response.StatusCode}";
                return View(new List<SaleInvoice>());
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error: " + ex.Message;
                return View(new List<SaleInvoice>());
            }
        }
    }
}