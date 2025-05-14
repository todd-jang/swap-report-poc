package com.swapreporting.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<SwapTrade, String> {
}