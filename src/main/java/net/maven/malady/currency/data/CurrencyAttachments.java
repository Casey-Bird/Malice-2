package net.maven.malady.currency.data;


import net.maven.malady.Malady;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CurrencyAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Malady.MODID);

    public static final Supplier<AttachmentType<CurrencyData>> CURRENCY_DATA =
            ATTACHMENT_TYPES.register("currency_data",
                    () -> AttachmentType.serializable(CurrencyData::new).build());

}
