package com.enigma.challenge.tokohapeya.service.implement;

import com.enigma.challenge.tokohapeya.dto.request.CreateTransactionRequest;
import com.enigma.challenge.tokohapeya.dto.response.*;
import com.enigma.challenge.tokohapeya.entity.*;
import com.enigma.challenge.tokohapeya.repository.TransactionRepo;
import com.enigma.challenge.tokohapeya.service.ProductService;
import com.enigma.challenge.tokohapeya.service.ProfileService;
import com.enigma.challenge.tokohapeya.service.TransactionDetailService;
import com.enigma.challenge.tokohapeya.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;

    private final TransactionDetailService transactionDetailService;
    private final ProfileService profileService;
    private final ProductService productService;

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .brand(product.getBrand())
                .type(product.getPhoneType())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    private AccountResponse toAccountResponse(UserAccount account) {
        return AccountResponse.builder()
                .accountId(account.getId())
                .email(account.getEmail())
                .username(account.getUsername())
                .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    private ProfileResponse toProfileResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .account(toAccountResponse(profile.getAccount()))
                .name(profile.getName())
                .address(profile.getAddress())
                .birthDate(profile.getBirthDate())
                .createdAt(profile.getCreatedAt())
                .build();
    }

    private TransactionDetailResponse toTransactionDetailResponse(TransactionDetail detail) {
        return TransactionDetailResponse.builder()
                .id(detail.getId())
                .product(toProductResponse(detail.getProduct()))
                .quantity(detail.getQuantity())
                .total(detail.getTotalPrice())
                .build();
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        List<TransactionDetailResponse> trxDetailRes = transaction.getTransactionDetails().stream()
                .map(this::toTransactionDetailResponse)
                .toList();
        return TransactionResponse.builder()
                .id(transaction.getId())
                .user(toProfileResponse(transaction.getUser()))
                .amount(transaction.getAmount())
                .isPaid(transaction.getPaid())
                .transactionDate(transaction.getDate())
                .transactionDetails(trxDetailRes)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(CreateTransactionRequest request) {
        Profile profileById = profileService.getProfileById(request.getUserId());

        Transaction transaction = Transaction.builder()
                .user(profileById)
                .date(LocalDateTime.now())
                .paid(false)
                .build();

        List<TransactionDetail> transactionDetails = request.getTransactionDetails().stream()
                .map(detail -> {
                    Product product = productService.getProductById(detail.getProductId());
                    if (product.getStock() - detail.getQuantity() < 0) {
                        throw new RuntimeException("Out of stock");
                    }
                    product.setStock(product.getStock() - detail.getQuantity());
                    return TransactionDetail.builder()
                            .product(product)
                            .transaction(transaction)
                            .quantity(detail.getQuantity())
                            .totalPrice(detail.getQuantity() * product.getPrice())
                            .build();
                }).toList();
        transactionDetailService.createBulk(transactionDetails);

        transaction.setTransactionDetails(transactionDetails);
        transaction.setAmount(transactionDetails.stream()
                .mapToLong(TransactionDetail::getTotalPrice)
                .reduce(0, Long::sum));
        transactionRepo.saveAndFlush(transaction);

        return toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getById(String id) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));
        return toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAll() {
        return transactionRepo.findAll().stream().map(this::toTransactionResponse).toList();
    }
}
