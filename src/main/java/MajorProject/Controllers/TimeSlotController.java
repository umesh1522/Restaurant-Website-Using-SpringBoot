package MajorProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class TimeSlotController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/addTimeslot")
    public String addTimeSlot(@RequestParam String date,
                              @RequestParam String timeSlot,
                              @RequestParam String tableNumber,
                              @RequestParam int seatingCapacity,
                              Model model) {
        String sql = "INSERT INTO timeslots (date, time_slot, table_number, seating_capacity) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, date, timeSlot, tableNumber, seatingCapacity);
        model.addAttribute("success", true);
        return "redirect:/admin?added=true";
    }

    @GetMapping("/admin")
    public String adminForm(Model model, @RequestParam(required = false) String added) {
        String sql = "SELECT * FROM timeslots";
        List<Map<String, Object>> slots = jdbcTemplate.queryForList(sql);
        model.addAttribute("slots", slots);
        model.addAttribute("added", added != null);
        return "admin_timeslots";
    }

    @GetMapping("/timeslots")
    public String userView(Model model) {
        String sql = "SELECT * FROM timeslots";
        List<Map<String, Object>> slots = jdbcTemplate.queryForList(sql);
        model.addAttribute("slots", slots);
        return "user_timeslots";
    }

    @PostMapping("/cancelSlot")
    public String cancelSlot(@RequestParam int id) {
        jdbcTemplate.update("DELETE FROM timeslots WHERE id = ?", id);
        return "redirect:/admin?cancelled=true";
    }
}
