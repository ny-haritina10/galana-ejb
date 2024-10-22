using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using boutique.Models;
using System.Linq;

namespace boutique.Controllers
{
    public class InvoiceController : Controller
    {
        private readonly HttpClient _httpClient;

        public InvoiceController()
        {
            _httpClient = new HttpClient();
            _httpClient.BaseAddress = new Uri("http://localhost:8080/");
        }

        public async Task<IActionResult> Index()
        {
            try
            {
                var response = await _httpClient.GetAsync("galana-ejb/api/invoices");
                if (response.IsSuccessStatusCode)
                {
                    var jsonString = await response.Content.ReadAsStringAsync();
                    var invoiceItems = JsonConvert.DeserializeObject<List<InvoiceItem>>(jsonString);

                    if (invoiceItems != null && invoiceItems.Any())
                    {
                        var invoices = invoiceItems
                            .GroupBy(i => i.OrderId)
                            .Select(g => new Invoice
                            {
                                OrderId = g.Key,
                                ClientId = g.First().ClientId,
                                OrderDate = DateTime.Parse(g.First().OrderDate),
                                TotalInvoiceAmount = g.First().TotalInvoiceAmount
                            })
                            .ToList();

                        return View(invoices);
                    }
                }
                
                ViewBag.Error = $"Could not fetch invoice data. Status: {response.StatusCode}";
                return View(new List<Invoice>());
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error: " + ex.Message;
                return View(new List<Invoice>());
            }
        }

        public async Task<IActionResult> Details(int id)
        {
            try
            {
                var response = await _httpClient.GetAsync("galana-ejb/api/invoices");
                if (response.IsSuccessStatusCode)
                {
                    var jsonString = await response.Content.ReadAsStringAsync();
                    var invoiceItems = JsonConvert.DeserializeObject<List<InvoiceItem>>(jsonString);

                    var invoice = invoiceItems
                        .Where(i => i.OrderId == id)
                        .GroupBy(i => i.OrderId)
                        .Select(g => new Invoice
                        {
                            OrderId = g.Key,
                            ClientId = g.First().ClientId,
                            OrderDate = DateTime.Parse(g.First().OrderDate),
                            Items = g.ToList(),
                            TotalInvoiceAmount = g.First().TotalInvoiceAmount
                        })
                        .FirstOrDefault();

                    if (invoice != null)
                    {
                        return View(invoice);
                    }
                }
                
                ViewBag.Error = $"Could not fetch invoice data. Status: {response.StatusCode}";
                return View(new Invoice());
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error: " + ex.Message;
                return View(new Invoice());
            }
        }
    }
}