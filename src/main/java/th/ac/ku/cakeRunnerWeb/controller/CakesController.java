package th.ac.ku.cakeRunnerWeb.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import th.ac.ku.cakeRunnerWeb.model.Cakes;
import th.ac.ku.cakeRunnerWeb.model.Order;
import th.ac.ku.cakeRunnerWeb.service.CakesService;
import th.ac.ku.cakeRunnerWeb.service.UserService;

import java.util.UUID;

@Controller
@RequestMapping("/cakes")
public class CakesController {

    @Autowired
    private CakesService cakesService;
    @Autowired
    private UserService userServices;


    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable UUID id, Model model) {
        Cakes cakes = cakesService.getOneById(id);
        model.addAttribute("cakes", cakes);
        return "cakes-edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute Cakes cakes, Model model) {
//        if(checkCake(cakes.getPrice(), cakes.getProductQuantity(), cakes.getProductDiscountPercent(), cakes.getPriceExcludingVat(), cakes.getPricePromotion())){
        cakesService.update(cakes);
//        }
        return "redirect:/cakes";
    }

    @GetMapping
    public String getCakes(Model model, Authentication authentication)
    {
        userServices.setLoginUser(authentication.getName());
        model.addAttribute("cakes", cakesService.getAll());
        return "cakes";
    }
    @GetMapping("/add")
    public String getAddForm(){
        return "cakes-add";
    }

    @PostMapping("/add")
    public String addCakes(@ModelAttribute Cakes cakes, Model model, RedirectAttributes redirectAttrs) {
        if(checkCake(cakes.getPrice(), cakes.getProductQuantity(), cakes.getProductDiscountPercent(), cakes.getPriceExcludingVat(), cakes.getPricePromotion())){
            if(checkAddress(cakes.getProductName(), cakes.getProductCategory(), cakes.getPrice(),cakes.getPoID()
                    , cakes.getProductDescription(), cakes.getProductAttrib(), cakes.getProductUsageGuideline()
                    ,cakes.getProductIngredients(), cakes.getProductNutrition(), cakes.getProductUseIndication()
                    , cakes.getProductQuantity(),cakes.getProductSize(), cakes.getProductVolume(), cakes.getProductWeight()
                    , cakes.getProductPromotion(), cakes.getProductDiscountPercent(),cakes.getPriceExcludingVat()
                    , cakes.getPricePromotion(), cakes.getPcID(), cakes.getPrr_ID(), cakes.getPsvID()
                    ,cakes.getFtvID(), cakes.getaID(), cakes.getRreID())) {
                cakesService.addCakes(cakes);
                return "redirect:/cakes";
            }
            else {
                redirectAttrs.addFlashAttribute("error","Please fill all the information fields!");
                return "redirect:/cakes/add";
            }
        }
        else {
            redirectAttrs.addFlashAttribute("error","negative number is not allowed!");
            return "redirect:/cakes/add";
        }
    }

    public boolean checkCake(double price, int productQuantity, double productDiscountPercent, double priceExcludingVat, double pricePromotion){ //Method ดักห้ามใส่จำนวนเป็น 0
        if(price > 0 && productQuantity > 0 && productDiscountPercent > 0 && priceExcludingVat > 0 && pricePromotion > 0){
            return true;
        }
        return false;
    }

    public boolean checkAddress(String productName, String productCategory, double price, String poID, String productDescription //Method ดักให้ใส่ข้อมูลให้ครบ
            ,String productAttrib, String productUsageGuideline, String productIngredients, String productNutrition, String productUseIndication
            ,int productQuantity, String productSize, String productVolume, String productWeight, String productPromotion, double productDiscountPercent
            ,double priceExcludingVat, double pricePromotion, String pcID, String prr_ID, String psvID, String ftvID, String aID, String rreID){
        if (productName.equals("") || productCategory.equals("") || String.valueOf(price).equals("") || poID.equals("") || productDescription.equals("")
        || productAttrib.equals("") || productUsageGuideline.equals("") || productIngredients.equals("") || productNutrition.equals("")
        || productUseIndication.equals("") || String.valueOf(productQuantity).equals("") || productSize.equals("") || productVolume.equals("")
        || productWeight.equals("") || productPromotion.equals("") || String.valueOf(productDiscountPercent).equals("")
        || String.valueOf(priceExcludingVat).equals("") || String.valueOf(pricePromotion).equals("") || pcID.equals("") || prr_ID.equals("")
        || psvID.equals("") || ftvID.equals("") || aID.equals("") || rreID.equals("")){
            return false;
        }return true;
    }
}
