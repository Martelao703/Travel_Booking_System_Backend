package com.david.travel_booking_system.config.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final List<DataSeeder> seeders;

    public DataLoader(List<DataSeeder> seeders) {
        this.seeders = seeders;
    }

    @Override
    public void run(String... args) {
        // Execute each seeder in order
        for (DataSeeder seeder : seeders) {
            seeder.seed();
        }
        System.out.println("Dev data seeding complete.");
    }
}
