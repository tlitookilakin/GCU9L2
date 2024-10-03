package co.grandcircus.digipets;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class AccountController {
   
    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/Accounts")
    public List<Account> getAll() {
        return  accountRepo.findAll();
    }

    @GetMapping("/Accounts/{id}")
    public Account getById(@PathVariable("id") Long id) {
        return accountRepo.findById(id).orElse(null);
    }

    @GetMapping("/Accounts/by-key/{apiKey}")
    public Account getByApiKey(@PathVariable String apiKey) {
        return accountRepo.findByApiKey(apiKey);
    }

    @PostMapping("/Accounts")
    public Account postAccount(@RequestBody Account entity) {
        entity.setId(null);
        entity.setApiKey(UUID.randomUUID().toString());
        accountRepo.save(entity);
        return entity;
    }
    
    @DeleteMapping("/Accounts/{id}")
    public void deleteAccount(@PathVariable("id") Long id){
        accountRepo.deleteById(id);
    }
    
    @PutMapping("Accounts/{id}/add-credits")
    public Account addCredits(@PathVariable Long id, @RequestBody String entity, @RequestParam(required = true) Integer credits) {
        Account addCredits = accountRepo.findById(id).orElse(null);
        addCredits.setCredits(credits + addCredits.getCredits());
        accountRepo.save(addCredits);
        return addCredits;
    }

    @PostMapping("/Accounts/{id}/action")
    public Account postAction(@PathVariable Long id, @RequestParam(required = true) String action) {
        Integer amount = 0;
        switch(action){
            case "CREATE": amount = -5;
            break;
            case "HEAL": amount = -1;
            break;
            case "TRAIN": amount = -1;
            break;
            case "BATTLE": amount = -2;
            break;
        }
        Optional<Account> addCredits = accountRepo.findById(id);
        if(addCredits.isPresent()){
            Account addTo = addCredits.get();
            addTo.setCredits(amount + addTo.getCredits());
            accountRepo.save(addTo);
            return addTo;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "acount not found");
    }

    @PostMapping("/Accounts/by-key/{apiKey}/action")
    public Account postActionApi(@PathVariable String apiKey, @RequestParam(required = true) String action) {
        Integer amount = 0;
        switch(action){
            case "CREATE": amount = -5;
            break;
            case "HEAL": amount = -1;
            break;
            case "TRAIN": amount = -1;
            break;
            case "BATTLE": amount = -2;
            break;
        }
        Account addCredits = accountRepo.findByApiKey(apiKey);
        if(addCredits != null){
            Account addTo = addCredits;
            addTo.setCredits(amount + addTo.getCredits());
            accountRepo.save(addTo);
            return addTo;
        }
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "acount not found");
    }
    

    

    
    
}
