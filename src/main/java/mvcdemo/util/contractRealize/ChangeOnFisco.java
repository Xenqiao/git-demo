package mvcdemo.util.contractRealize;

import mvcdemo.po.ProUserDO;
import mvcdemo.po.ProductDO;
import mvcdemo.po.UserDO;
import mvcdemo.util.toolcontract.Erc20;
import mvcdemo.util.toolcontract.ProUser;
import mvcdemo.util.toolcontract.Product;
import mvcdemo.util.toolcontract.User;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Xenqiao
 * @create 2023/3/21 21:44
 */
public class ChangeOnFisco {
    public ChangeOnFisco() { }
    /**区块链的初始化
     *
     */

    public String CreatingBlocks(String contractName){
        try {
            AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(GetBcosSDK.getClient(), GetBcosSDK.getKeyPair(), "src/main/resources/solidity/abi", "src/main/resources/solidity/bin");
            TransactionResponse response = transactionProcessor.deployByContractLoader(contractName, new ArrayList<>());
            String Address = response.getContractAddress();
            return Address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /** 一个在链上改变用户信息的函数 */
    public void ChangeUserOnFisco(UserDO userDO){

        String Address = userDO.getHash();
        User user = new User(Address, GetBcosSDK.getClient(), GetBcosSDK.getKeyPair());
        user.setUser(
                userDO.getHash(),
                userDO.getUserName(),
                userDO.getName(),
                userDO.getPhone(),
                userDO.getHome()
        );
    }


    /** 一个在链上改变生产商信息的函数 */
    public void ChangeProUserOnFisco(ProUserDO proUserDO){

            String Address = proUserDO.getHash();
            ProUser proUser = new ProUser(Address,GetBcosSDK.getClient(),GetBcosSDK.getKeyPair());
            proUser.setProUser(
                    proUserDO.getHash(),
                    proUserDO.getUserName(),
                    proUserDO.getProManager(),
                    proUserDO.getProPhone(),
                    proUserDO.getProHome()
            );

    }


    /** 一个在链上创建产品信息的函数 */
    public void GetProductHash(ProductDO productDO){

        String Address = new ChangeOnFisco().CreatingBlocks("Product");
        Product product = new Product(Address, GetBcosSDK.getClient(), GetBcosSDK.getKeyPair());

        productDO.setProductHash(product.getContractAddress());
        product.setProduct(
                productDO.getProductHash(),
                productDO.getProductName(),
                productDO.getProductPrice(),
                productDO.getProductPlace(),
                productDO.getProductMake(),
                productDO.getProductId()
        );
    }


    public String CreateErc20(String hash){

        String a = "0x10e47f5aa26b58f53e055878148ed47529a24099";
        String contractAddress = "0x3fb7e04e2ff1a5796ff62348c6dc2b05f684118c";

        //String contractAddress = new ChangeOnFisco().CreatingBlocks("Erc20");

        Erc20 erc20 = new Erc20(contractAddress, GetBcosSDK.getClient(), GetBcosSDK.getKeyPair());
        try {
            //erc20.transferFrom(a,hash,BigInteger.valueOf(500));
            return erc20.balanceOf(hash).toString();

        } catch (ContractException e) {
            e.printStackTrace();
        }
        return "";

    }
}
