CREATE OR REPLACE PROCEDURE create_accounts()
LANGUAGE plpgsql
AS $$
BEGIN
    FOR i IN 1..200 LOOP
        INSERT INTO public.account (creation_time, balance, active, user_id)
        VALUES (
            current_timestamp,
            1000 * i,
            true,
            (SELECT id FROM public.client OFFSET floor(random() * (SELECT COUNT(*) FROM public.client)) LIMIT 1)
        );
    END LOOP;
END;
$$;

CALL create_accounts();