package com.sbhandare.pawdopt.Controller;

import com.sbhandare.pawdopt.DTO.PetDTO;
import com.sbhandare.pawdopt.Service.PetService;
import com.sbhandare.pawdopt.Util.PawdoptConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getAllPets(@RequestParam(value = "orgid", required = false) Integer orgid, @RequestParam(value =
            "userid", required = false) Integer uid) {
        if (orgid != null && uid == null) {
            List<PetDTO> allPetsByOrgId = petService.getPetsByOrgId(orgid);
            if (allPetsByOrgId == null || allPetsByOrgId.isEmpty())
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            return allPetsByOrgId;
        } else if (orgid == null && uid != null) {
            List<PetDTO> allPetsByUser = petService.getPetsByUserId(uid);
            if (allPetsByUser == null || allPetsByUser.isEmpty())
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
            return allPetsByUser;
        }
        List<PetDTO> allPetList = petService.getAllPets();
        if (allPetList == null || allPetList.isEmpty())
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        return allPetList;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object getPet(@PathVariable(value = "id") int petid) {
        PetDTO petDTO = petService.getPetById(petid);
        if (petDTO == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        return petDTO;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Object createPet(@Valid @RequestBody PetDTO petDTO, @RequestParam(name = "orgid") int orgid) {
        int petid = petService.savePet(petDTO,orgid);
        if(petid == PawdoptConstantUtil.NO_SUCCESS)
            return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
